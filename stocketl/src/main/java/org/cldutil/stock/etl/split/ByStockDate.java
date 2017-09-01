package org.cldutil.stock.etl.split;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.datacrawl.CrawlConf;
import org.cldutil.etl.fci.AbstractCrawlItemToCSV;
import org.cldutil.stock.common.StockConfig;
import org.cldutil.stock.common.StockUtil;
import org.cldutil.stock.etl.ETLUtil;
import org.cldutil.stock.etl.base.ETLConfig;
import org.cldutil.stock.persistence.StockPersistMgr;
import org.cldutil.taskmgr.TaskUtil;
import org.cldutil.taskmgr.entity.Task;
import org.cldutil.taskmgr.hadoop.HadoopTaskLauncher;
import org.cldutil.util.DateTimeUtil;
import org.cldutil.util.StringUtil;

public class ByStockDate extends SplitTask{
	private static Logger logger =  LogManager.getLogger(ByStockDate.class);
	
	private String marketId;
	private List<String> stockids;
	private Date sd;
	private Date endDate;
	
	public ByStockDate(ETLConfig ec, CrawlConf cconf, String propfile, Task t,  String cmd, Map<String, Object> params,
			String marketId, List<String> stockids, Date sd, Date ed){
		super(ec, cconf, propfile, t, cmd, params);
		
		this.marketId = marketId;
		this.stockids = stockids;
		this.sd = sd;
		this.endDate = ed;
	}

	@Override
	public List<Task> getTasks() {
		//you should not call this
		return null;
	}

	@Override
	public String getTaskName() {
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(AbstractCrawlItemToCSV.FN_MARKETID, marketId);
		taskParams.put(AbstractCrawlItemToCSV.FN_STARTDATE, sd);
		taskParams.put(AbstractCrawlItemToCSV.FN_ENDDATE, endDate);
		return ETLUtil.getTaskName(ec, cmd, taskParams);
	}
	
	@Override
	public String genInputFile(){
		try {
			logger.info(String.format("into runTaskByStockDate with marketId:%s", marketId));
			Map<String, Date> stockLUMap = new HashMap<String, Date>();
			if (ETLUtil.needCheckDB(ec, cconf, cmd)){
				String query = ec.getStockLUDateSqlByCmd(cmd);
				stockLUMap = StockPersistMgr.getStockLUDateByCmd(query, cconf.getBigdbconf());
			}
			LinkedList<Date> cacheDates = new LinkedList<Date>();
			List<String> jobIdList = new ArrayList<String>();
			StockConfig sc = StockUtil.getStockConfig(marketId);
			//
			Configuration conf = HadoopTaskLauncher.getHadoopConf(cconf);
			FileSystem fs = FileSystem.get(conf);
			String escapedName = StringUtil.escapeFileName(getTaskName());
			String taskFileName = cconf.getHdfsTaskFolder() + "/" + escapedName;
			Path fileNamePath = new Path(taskFileName);
			OutputStream fin = fs.create(fileNamePath);
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fin, "UTF-8" ));
			
			for (String id: stockids){
				Date sd = null;
				String idForDB = ec.stockIdCmd2DB(id, cmd);
				if (stockLUMap.containsKey(idForDB)){
					sd = stockLUMap.get(idForDB);
					if (sd!=null){//since we crawl [startDate, endDate)
						sd = DateTimeUtil.tomorrow(sd);
					}
				}
				if (sd==null){
					//need to open url for each year and quarter, so we need to use ipo start date.
					sd = ETLUtil.getIPODateByStockId(ec, marketId, id, cmd);
				}
				logger.info(String.format("%s: start date for cmd %s is %s", id, cmd, ec.getSdf().format(sd)));
				//update cache if necessary
				Date cacheFirstDate = null;
				if (!cacheDates.isEmpty())
					cacheFirstDate = cacheDates.getFirst();
				else{
					if (endDate==null)
						cacheFirstDate = new Date();
					else{
						cacheFirstDate = endDate;
					}
				}
				LinkedList<Date> dll = StockUtil.getOpenDayList(sd, cacheFirstDate, sc.getHolidays());
				cacheDates.addAll(0, dll);
				//get dates from cache
				Date firstWorkingDay = null;
				if (StockUtil.isOpenDay(sd, sc.getHolidays())){
					firstWorkingDay = sd;
				}else{
					firstWorkingDay = StockUtil.getNextOpenDay(sd, sc.getHolidays());
				}
				int idx = cacheDates.indexOf(firstWorkingDay);
				if (idx==-1){
					logger.error("cache dates do not contains:" + firstWorkingDay);
				}else{
					Iterator<Date> tryDates = cacheDates.listIterator(idx);
					while(tryDates.hasNext()){
						Date d = tryDates.next();
						String dstr = ec.getSdf().format(d);
						Task t1 = t.clone(ETLUtil.class.getClassLoader());
						t1.putParam("stockid", id);
						t1.putParam("date", dstr);
						t1.putAllParams(params);
						br.write(TaskUtil.taskToJson(t));
						br.write("\n");
					}
				}
			}
			br.close();
			return taskFileName;
		}catch(Exception e){
			logger.error("", e);
			return null;
		}
	}
}
