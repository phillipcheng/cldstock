package org.cldutil.stock.etl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.cldutil.datacrawl.CrawlConf;
import org.cldutil.datacrawl.task.BrowseProductTaskConf;
import org.cldutil.etl.fci.AbstractCrawlItemToCSV;
import org.cldutil.taskmgr.TaskMgr;
import org.cldutil.taskmgr.TaskResult;
import org.cldutil.taskmgr.TaskUtil;
import org.cldutil.taskmgr.entity.Task;
import org.cldutil.taskmgr.hadoop.HadoopTaskLauncher;
import org.cldutil.taskmgr.hadoop.TaskMapper;
import org.cldutil.util.entity.CrawledItem;
import org.cldutil.util.jdbc.JDBCMapper;
import org.cldutil.util.jdbc.PersistObject;
import org.cldutil.stock.etl.base.ETLConfig;
import org.cldutil.stock.etl.split.ByDate;
import org.cldutil.stock.etl.split.ByMarket;
import org.cldutil.stock.etl.split.ByMarketYearMonth;
import org.cldutil.stock.etl.split.ByStock;
import org.cldutil.stock.etl.split.ByStockDate;
import org.cldutil.stock.etl.split.ByStockYearQuarter;
import org.cldutil.stock.etl.split.SplitTask;
import org.cldutil.xml.mytaskdef.ParsedBrowsePrd;
import org.cldutil.xml.taskdef.BrowseDetailType;

public class ETLUtil {
	
	private static Logger logger =  LogManager.getLogger(ETLUtil.class);
	
	//for task name
	public static final String PK_STOCKID="stockid";
	public static final String PK_DATE="date";
	public static final String PK_LOOKAHEADE="lookahead";//how many time unit (quarter) to look ahead, can be 0, 1, 2, etc
	public static final String PK_MAPPER="mapper";
	public static final String PK_REDUCER="reducer";

	public static final int maxBatchSize=1000000;
	//parameter keys define in the browseTask
	
	public static final Class DEFAULT_MAPPER=TaskMapper.class;
	private static Map<String, Map<String, Date>> ipoCache = new HashMap<String,Map<String,Date>>();//marketid to <stockid to ipodate> cache
	
	public static void cleanCaches(){
		ipoCache=null;
	}
	
	public static String getStartUrl(Task t){
		ParsedBrowsePrd btt = t.getBrowseDetailTask(t.getName());
		return btt.getBrowsePrdTaskType().getBaseBrowseTask().getStartUrl().getValue();
	}
	
	public static boolean isStatic(Task t){
		ParsedBrowsePrd btt = t.getBrowseDetailTask(t.getName());
		if (btt.getParamMap().containsKey(AbstractCrawlItemToCSV.FIELD_NAME_STATIC) && 
				"true".equals(btt.getParamMap().get(AbstractCrawlItemToCSV.FIELD_NAME_STATIC).getValue())){
			return true;
		}
		return false;
	}

	public static boolean isStatic(ETLConfig sc, CrawlConf cconf, String cmdName){
		String confName = sc.getCrawlByCmd(cmdName);
		List<Task> tl = cconf.setUpSite(confName+".xml", null);
		if (tl.size()==1){
			return isStatic(tl.get(0));
		}else{
			logger.error(String.format("isStatic cmd %s does not contain 1 crawlTask.", cmdName));	
		}
		return false;
	}
	
	public static int getLookahead(Task t){
		ParsedBrowsePrd btt = t.getBrowseDetailTask(t.getName());
		if (btt.getParamMap().containsKey(PK_LOOKAHEADE)){
			return Integer.parseInt(btt.getParamMap().get(PK_LOOKAHEADE).getValue());
		}else{
			return 1; //default lookahead 1 quarter
		}
	}
	
	public static boolean needOverwrite(CrawlConf cconf, String cmdName){
		return false;
	}
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static String getTaskName(ETLConfig sc, String cmd, Map<String, Object> params){
		StringBuffer sb = new StringBuffer();
		sb.append(cmd);
		sb.append("_");
		if (params!=null){
			for (String key: params.keySet()){
				sb.append(key);
				sb.append("_");
				Object val = params.get(key);
				if (val!=null){
					String sval = null;
					if (val instanceof Date){
						sval = sc.getSdf().format(val);
					}else{
						sval = val.toString();
					}
					sb.append(sval);
				}else{
					sb.append("null");
				}
				sb.append("_");
			}
		}
		sb.append(sdf.format(new Date()));
		return sb.toString();
	}
	
	public static boolean needCheckDB(ETLConfig sc, CrawlConf cconf, String cmd){
		if (cconf.getBigdbconf().getUrl()==null)
			return false;
		if (isStatic(sc, cconf, cmd)){
			return false;
		}else if (Arrays.asList(sc.getCurrentDayCmds()).contains(cmd)){
			return false; //skip for current day cmd
		}
		return true;
	}
	
	public static Date getIPODateByStockId(ETLConfig sc, String marketId, String stockId, String cmd){
		if (!ipoCache.containsKey(marketId)){
			ipoCache.put(marketId, sc.getStockMetaStore().getStockIPOData(marketId));
		}
		stockId = sc.stockIdCmd2DB(stockId, cmd);
		Date d = ipoCache.get(marketId).get(stockId);
		if (d==null){
			d = sc.getMarketStartDate();
		}
		logger.debug(String.format("stock %s ipo date: %s", stockId, sc.getSdf().format(d)));
		return d;
	}
	
	private static Date getDate(ETLConfig sc, String key, Map<String, Object> params){
		try{
			String strVal = (String)params.get(key);
			if (strVal!=null){
				return sc.getSdf().parse((String)params.get(key));
			}else{
				return null;
			}
		}catch(Exception e){
			logger.error("", e);
			return null;
		}
	}
	
	//called by run cmd hadoop-less
	public static String[] runTaskByCmd(ETLConfig sc, String marketId, List<String> stockids, CrawlConf cconf, String propfile, 
			String cmdName, Map<String, Object> params){
		boolean isStatic = ETLUtil.isStatic(sc, cconf, cmdName);
		return runTaskByCmd(sc, marketId, stockids, cconf, propfile, cmdName, params, isStatic);
	}
	
	private static List<String> getCsvList(List<CrawledItem> cil){
		List<String> outputList = new ArrayList<String>();
		for (CrawledItem ci:cil){
			if (ci!=null && ci.getCsvValue()!=null){
				//key,value,filename
				for (String[] kv : ci.getCsvValue()){
					if (AbstractCrawlItemToCSV.KEY_VALUE_UNDEFINED.equals(kv[0])){
						outputList.add(kv[1]);
					}else{
						String str = String.format("%s,%s", kv[0], kv[1]);
						outputList.add(str);
					}
				}
			}
		}
		return outputList;
	}
	
	//all will be filtered with startDate, endDate
	//params in the task def:						method                       sample
	//marketid, stockid, year, quarter:	runTaskByStockYearQuarter	BalanceSheet, CashFlow, sina-fq, sina-historical
	//marketid, stockid, date         : runTaskByStockDate			sina-stock-market-tradedetail
	//marketid, stockid,			  :	runTaskByStock				holding-insiders, dividend-history, quote-historical, short-interest, corp-info, nasdaq-fq
	//marketid, date                  : runTaskByDate				sina-stock-market-dzjy, rzrq, nasdaq-upcoming-dividend
	//marketid                        : runTaskByMarket		        stock-ids
	//marketid, stockid, year, month  : runTaskByMarketYearMonth    sina-stock-ipo
	/**
	 * @param ec
	 * @param marketId
	 * @param stockids
	 * @param cconf
	 * @param propfile
	 * @param cmdName
	 * @param params
	 * @param staticCmd: true: not using hadoop, return the csv synchronously, and store in the metastore
	 * @return
	 */
	public static String[] runTaskByCmd(ETLConfig ec, String marketId, List<String> stockids, CrawlConf cconf, String propfile, 
			String cmdName, Map<String, Object> params, boolean staticCmd){
		List<Task> tl = new ArrayList<Task>();
		
		String confFileName = ec.getCrawlByCmd(cmdName) + ".xml";
		tl = cconf.setUpSite(confFileName, null);
		
		List<String> jobIds = new ArrayList<String>();
		for (Task t: tl){
			//get the bpt definition out of the t
			t.initParsedTaskDef();
			t.putAllParams(params);
			BrowseDetailType bdt = t.getBrowseDetailTask(t.getName()).getBrowsePrdTaskType();
			BrowseProductTaskConf.evalParams(t, bdt);
			ParsedBrowsePrd btt = t.getBrowseDetailTask(t.getName());
			if (btt.getParamMap().containsKey(AbstractCrawlItemToCSV.FN_MARKETID)){
				params.put(AbstractCrawlItemToCSV.FN_MARKETID, marketId);
				Date sd = getDate(ec, AbstractCrawlItemToCSV.FN_STARTDATE, params);
				Date ed = getDate(ec, AbstractCrawlItemToCSV.FN_ENDDATE, params);
				SplitTask st = null;
				if (btt.getParamMap().containsKey(PK_STOCKID)){
					if (btt.getParamMap().containsKey(AbstractCrawlItemToCSV.FN_YEAR) && btt.getParamMap().containsKey(AbstractCrawlItemToCSV.FN_QUARTER)){
						st = new ByStockYearQuarter(ec, cconf, propfile, t, cmdName, params, marketId, stockids, sd, ed);
					}else if (btt.getParamMap().containsKey(PK_DATE)){
						st = new ByStockDate(ec, cconf, propfile, t, cmdName, params, marketId, stockids, sd, ed);
					}else{
						st = new ByStock(ec, cconf, propfile, t, cmdName, params, marketId, stockids);
					}
				}else{
					if (btt.getParamMap().containsKey(AbstractCrawlItemToCSV.FN_YEAR) && btt.getParamMap().containsKey(AbstractCrawlItemToCSV.FN_MONTH)){
						st = new ByMarketYearMonth(ec, cconf, propfile, t, cmdName, params, sd, ed);
					}else if (btt.getParamMap().containsKey(PK_DATE)){
						st = new ByDate(ec, cconf, propfile, t, cmdName, params, sd, ed);
					} else{
						st = new ByMarket(ec, cconf, propfile, t, cmdName, params);
					}
				}
				//execute
				Class mapperClass = DEFAULT_MAPPER;
				Class reducerClass = null;
				try{
					if (btt.getParamMap().containsKey(PK_MAPPER)){
						mapperClass = Class.forName(btt.getParamMap().get(PK_MAPPER).getValue());
					}
					if (btt.getParamMap().containsKey(PK_REDUCER)){
						reducerClass = Class.forName(btt.getParamMap().get(PK_REDUCER).getValue());
					}
				}catch(Exception e){
					logger.error("", e);
				}
				if (staticCmd){
					List<Task> tlist = st.getTasks();
					List<String> output = new ArrayList<String>();
					for (Task rt: tlist){
						params.put(TaskMgr.TASK_RUN_PARAM_CCONF, cconf);
						try {
							TaskResult tr = rt.runMyself(params, false, null, null);
							if (tr!=null){
								List<String> sl = getCsvList(tr.getCIs());
								output.addAll(sl);
							}
						}catch(Exception e){
							logger.error("", e);
						}
					}
					//output saved to metastore
					JDBCMapper jdbcMapper = ec.getJDBCMapper(cmdName);
					for (String csv:output){
						PersistObject obj = jdbcMapper.getObject(csv);
						ec.getStockMetaStore().addOrUpdate(obj);
					}
				}else{
					String taskFileName = st.genInputFile();
					boolean multipleOutput = HadoopTaskLauncher.hasMultipleOutput(t);
					String outputDir = t.getOutputDir(null, cconf);
					Map<String, String> hadoopParams = new HashMap<String, String>();
					hadoopParams.put(TaskUtil.TASKCONF_PROPERTIES, propfile);
					hadoopParams.put(AbstractCrawlItemToCSV.FN_MARKETID, (String) params.get(AbstractCrawlItemToCSV.FN_MARKETID));
					hadoopParams.put(AbstractCrawlItemToCSV.FN_ENDDATE, ec.getSdf().format(ed));
					HadoopTaskLauncher.updateHadoopParams(t, hadoopParams);
					String jobId = HadoopTaskLauncher.hadoopExecuteTasks(cconf, hadoopParams, new String[]{taskFileName}, 
							multipleOutput, outputDir, false, mapperClass, reducerClass, true);
					jobIds.add(jobId);
				}
			}else{
				logger.error(String.format("cmd %s has no mandatory field 'marketId'.", cmdName));
			}
		}
		String[] jobIdsArray = new String[]{};
		return jobIds.toArray(jobIdsArray);
	}
}
