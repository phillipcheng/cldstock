package org.cldutil.stock.etl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobID;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.cldutil.taskmgr.entity.RunType;
import org.cldutil.taskmgr.entity.Task;
import org.cldutil.taskmgr.hadoop.HadoopTaskLauncher;
import org.cldutil.util.DateTimeUtil;
import org.cldutil.util.ListUtil;
import org.cldutil.util.StringUtil;
import org.cldutil.util.entity.CrawledItem;
import org.cldutil.util.entity.CrawledItemId;
import org.cldutil.util.jdbc.ScriptRunner;
import org.cldutil.util.jdbc.SqlUtil;
import org.cldutil.stock.etl.base.ETLConfig;
import org.cldutil.stock.etl.task.LoadDBDataTask;
import org.cldutil.stock.etl.task.MergeTask;
import org.cldutil.stock.persistence.CmdStatus;
import org.cldutil.stock.persistence.MarketInfo;
import org.cldutil.stock.persistence.MarketInfoId;
import org.cldutil.stock.persistence.StockPersistMgr;
import org.cldutil.xml.taskdef.BrowseDetailType;
import org.cldutil.etl.fci.AbstractCrawlItemToCSV;
import org.cldutil.hadooputil.TransferHdfsFile;

import org.cldutil.datacrawl.CrawlConf;
import org.cldutil.datacrawl.client.ClientBase;
import org.cldutil.datacrawl.task.BrowseProductTaskConf;

public abstract class StockBase extends ClientBase{
	protected static Logger logger =  LogManager.getLogger(StockBase.class);
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final String AllCmdRun_STATUS="AllCmdRun";
	public static final String KEY_IDS = "ids";
	public static final String idKeySep = "_";
	public static final String DELTA_NAME="delta";
	public static final char EXCLUDE_MARK='|';
	public static final char INCLUDE_MARK='+';
	
	protected String marketId;
	protected String propFile;
	protected Date endDate; //of server timezone, since the static holidays, the dynamic date from hbase are all in server timezone
	protected Date startDate;
	protected String specialParam = null;//for special cmd to use as parameter
	private StockPersistMgr spm;
	
	public abstract boolean fqReady(Date today);

	public StockBase(String propFile, String baseMarketId, String marketId, Date sd, Date ed, String marketBaseId){
		super();
		this.marketId = marketId;
		super.setProp(propFile);
		this.propFile = propFile;
		this.startDate = sd;
		this.endDate = ed;
		this.spm = new StockPersistMgr();
		this.spm.init();
	}
	
	public String toString(){
		return String.format("marketId:%s, propFile:%s, startDate:%s, endDate:%s", marketId, propFile, startDate, endDate);
	}
	
	public Map<String, Object> getDateParamMap(String startDate, String endDate){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(AbstractCrawlItemToCSV.FN_STARTDATE, startDate);
		paramMap.put(AbstractCrawlItemToCSV.FN_ENDDATE, endDate);
		return paramMap;
	}
	
	private void addCsvValue(CrawledItem ciDelta){
		List<String> deltaIds = (List<String>) ciDelta.getParam(KEY_IDS);
		String[][] csvValue = new String[deltaIds.size()][];
		for (int i=0; i<deltaIds.size(); i++){
			csvValue[i] = new String[]{"", deltaIds.get(i)};
		}
		ciDelta.setCsvValue(csvValue);
	}
	
	/**
	 * public method for client to invoke directly
	 * @param cmdName
	 * @param marketId
	 * @param startDate: yyyy-MM-dd
	 * @param endDate: yyyy-MM-dd
	 * @return
	 * @throws ParseException 
	 */
	public String[] runCmdHadoopless(String cmdName, String marketId, String startDate, String endDate) {
		try {
			logger.info(String.format("runCmdHadoopless cmd:%s, market %s, start %s, end %s.", cmdName, marketId, startDate, endDate));
			ETLConfig sc = ETLConfig.getETLConfig(marketId);
			Map<String, Object> params = getDateParamMap(startDate, endDate);
			params.put(AbstractCrawlItemToCSV.FN_MARKETID, marketId);
			Date d = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
			MarketInfo mi = (MarketInfo) spm.get(MarketInfo.class, new MarketInfoId(marketId, d));
			if (mi == null){
				logger.error(String.format("market info for %s and %s not found.", marketId, d));
				return null;
			}else{
				return ETLUtil.runTaskByCmd(sc, marketId, mi.getStockIds(), cconf, this.getPropFile(), cmdName, params, true);
			}
		}catch(Exception e){
			logger.error("", e);
			return null;
		}
	}
	
	/**
	 * public method for client to invoke directly
	 * @param cmdName
	 * @param marketId
	 * @param startDate: yyyy-MM-dd
	 * @param endDate: yyyy-MM-dd
	 * @return
	 */
	public CmdStatus runCmd(String cmdName, String marketId, String startDate, String endDate) {
		try {
			Date d = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
			MarketInfo mi = (MarketInfo) spm.get(MarketInfo.class, new MarketInfoId(marketId, d));
			if (mi == null){
				logger.error(String.format("market info for %s and %s not found.", marketId, d));
				return null;
			}else{
				return runCmd(cmdName, marketId, mi.getStockIds(), startDate, endDate);
			}
		}catch(Exception e){
			logger.error("", e);
			return null;
		}
	}
	
	/**
	 * 
	 * @param cmdName: fileName of the site-conf without suffix, also the storeid
	 * @param marketId
	 * @param stockids
	 * @param startDate
	 * @param endDate
	 * @param isStatic: true for one time data, store in the metastore
	 * @return
	 */
	private CmdStatus runCmd(String cmdName, String marketId, List<String> stockids, String startDate, String endDate) {
		logger.info(String.format("runCmd cmd:%s, market %s, start %s, end %s.", cmdName, marketId, startDate, endDate));
		ETLConfig sc = ETLConfig.getETLConfig(marketId);
		if (Arrays.asList(sc.getCurrentDayCmds()).contains(cmdName)){
			//do not need start date for current day cmd, only end date needed for output folder name
		}else{
			if (startDate==null){
				startDate = sc.getStartDate(cmdName);
				if (startDate==null){
					startDate = sdf.format(sc.getMarketStartDate());
				}
			}
		}
		Map<String, Object> params = getDateParamMap(startDate, endDate);
		params.put(AbstractCrawlItemToCSV.FN_MARKETID, marketId);
		Date ed = null;
		try{
			if (endDate!=null){
				ed = sdf.parse(endDate);
			}
		}catch(Exception e){
			logger.error("", e);
		}
		logger.info(String.format("going to run cmd %s with ed:%s, sd:%s, marketId:%s", cmdName, endDate, startDate, marketId));
		CmdStatus cs = new CmdStatus(marketId, cmdName, ed);
		String[] jobIds = ETLUtil.runTaskByCmd(sc, marketId, stockids, cconf, this.getPropFile(), cmdName, params);
		for (String jobId:jobIds){
			if (jobId!=null){
				cs.getJsMap().put(jobId, 4);//PREPARE
			}
		}
		spm.addOrUpdate(cs);
		return cs;
	}
	
	private List<CmdStatus> runCmdGroup(String marketId, List<String> stockids, Date sd, Date ed, 
			CrawlCmdGroupType groupType, 
			CrawlCmdType cmdType, String[] includeCmds, String[] excludeCmds){
		ETLUtil.cleanCaches();
		List<CmdStatus> cmdStatusList = new ArrayList<CmdStatus>();
		ETLConfig sc = ETLConfig.getETLConfig(marketId);
		String strSd = null;
		if (sd!=null){
			strSd = sdf.format(sd);
		}
		String strEd = null;
		if (ed!=null){
			strEd = sdf.format(ed);
		}
		String[] cmds = sc.getAllCmds(groupType);
		for (String cmd:cmds){
			if (includeCmds!=null){
				if (!Arrays.asList(includeCmds).contains(cmd)){
					continue;
				}
			}
			if (excludeCmds!=null){
				if (Arrays.asList(excludeCmds).contains(cmd)){
					continue;
				}
			}
			boolean isStatic = ETLUtil.isStatic(sc, cconf, cmd);
			if ((cmdType == CrawlCmdType.nondynamic && isStatic) ||
					(cmdType == CrawlCmdType.dynamic && !isStatic) ||
					(cmdType == CrawlCmdType.any)){
				CmdStatus cs = runCmd(cmd, marketId, stockids, strSd, strEd);
				if (cs==null){
					logger.error(String.format("cmdstatus for cmd:%s, market %s, start %s, end %s is null.", cmd, marketId, strSd, strEd));
				}
				cmdStatusList.add(cs);
			}else{
				logger.error(String.format("cmd isStatic %b, CrawlCmdType:%s, not match", isStatic, cmdType));
			}
		}
		return cmdStatusList;
	}
	
	private CrawledItem run_browse_idlist(ETLConfig ec, String marketId, Date endDate) throws InterruptedException{
		if (ec==null){
			ec = ETLConfig.getETLConfig(marketId);
		}
		CrawledItem ci = null;
		List<Task> tl = cconf.setUpSite(ec.getStockIdsCmd() + ".xml", null);
		Task t = tl.get(0);
		t.initParsedTaskDef();
		BrowseDetailType bdt = t.getBrowseDetailTask(t.getName()).getBrowsePrdTaskType();
		t.putParam(AbstractCrawlItemToCSV.FN_MARKETID, this.marketId);
		BrowseProductTaskConf.evalParams(t, bdt);
		String storeId = (String) t.getParamMap().get(AbstractCrawlItemToCSV.FN_STOREID);
		if (ec.getTestMarketId().equals(marketId)){
			Date marketChangeDate = null;
			try {
				marketChangeDate = sdf.parse(ec.getTestMarketChangeDate());
			}catch(Exception e){
				logger.error("", e);
				return null;
			}
			if (endDate.before(marketChangeDate)){
				ci = new CrawledItem(CrawledItem.CRAWLITEM_TYPE, "default", 
						new CrawledItemId(marketId, ec.getStockIdsCmd(), endDate));
				ci.addParam(KEY_IDS, Arrays.asList(ec.getTestStockSet1()));
			}else{
				ci = new CrawledItem(CrawledItem.CRAWLITEM_TYPE, "default", 
						new CrawledItemId(marketId, ec.getStockIdsCmd(), endDate));
				ci.addParam(KEY_IDS, Arrays.asList(ec.getTestStockSet2()));
			}
			addCsvValue(ci);
		}else{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("marketId", marketId);
			ci = browsePrd(ec.getStockIdsCmd() + ".xml", null, null, params, RunType.onePrd).get(0);
			logger.debug("ci we got:" + ci);
			if (ec.getPairedMarket()!=null && ec.getPairedMarket().containsKey(marketId)){
				String pairMarketId = ec.getPairedMarket().get(marketId);
				//we need to add the st market as well
				params.put("marketId", pairMarketId);
				CrawledItem ciAdd = browsePrd(ec.getStockIdsCmd() + ".xml", null, null, params, RunType.onePrd).get(0);
				List<String> ids = (List<String>) ciAdd.getParam(KEY_IDS);
				List<String> idsOrg = (List<String>) ci.getParam(KEY_IDS);
				idsOrg.addAll(ids);
				ci.addParam(KEY_IDS, idsOrg);
			}
		}
		ci.addParam(AbstractCrawlItemToCSV.FN_STOREID, storeId);
		return ci;
	}
	
	public MarketInfo runIdsCmd(ETLConfig ec, String marketId, Date endDate) throws InterruptedException {
		CrawledItem ciIds = run_browse_idlist(ec, marketId, endDate);
		List<String> curIds = (List<String>) ciIds.getParam(KEY_IDS);
		MarketInfo mi = new MarketInfo(marketId, endDate, curIds);
		spm.addOrUpdate(mi);
		return mi;
	}

	public boolean cmdFinished(String jid){
		Configuration hconf = HadoopTaskLauncher.getHadoopConf(cconf);
		JobClient jobClient=null;
		try {
			jobClient = new JobClient(hconf);
		}catch(Exception e){
			logger.error("", e);
		}
		boolean finished=true;
		JobID jobId = JobID.forName(jid);
		try{
			RunningJob rjob = jobClient.getJob(jobId);
			int newStatus=1;
			if (rjob!=null){
				newStatus = rjob.getJobStatus().getState().getValue();
				logger.debug(String.format("job %s got status %d", jid, newStatus));
			}else{
				logger.info(String.format("job %s not found in jobClient.", jid));
			}
			if (newStatus==1 || newStatus==4){//1 preparation, 4 running
				finished=false;
			}
		}catch(Exception e){
			logger.error("", e);
			finished=false;
		}
		return finished;
	}
	
	public boolean cmdAllFinished(Map<String, Integer> jobStatusMap){
		Configuration hconf = HadoopTaskLauncher.getHadoopConf(cconf);
		JobClient jobClient=null;
		try {
			jobClient = new JobClient(hconf);
		}catch(Exception e){
			logger.error("", e);
		}
		boolean finished=true;
		if (jobStatusMap!=null){
			for (String jid: jobStatusMap.keySet()){
				JobID jobId = JobID.forName(jid);
				try{
					RunningJob rjob = jobClient.getJob(jobId);
					int orgStatus = jobStatusMap.get(jid);
					int newStatus = orgStatus;
					if (rjob!=null){
						newStatus = rjob.getJobStatus().getState().getValue();
						logger.debug(String.format("job %s got status %d", jid, newStatus));
					}else{
						logger.info(String.format("job %s not found in jobClient.", jid));
					}
					if (newStatus!=orgStatus){
						logger.info(String.format("job %s changed from status %d to status %s.", jid, orgStatus, newStatus));
						jobStatusMap.put(jid, newStatus);
					}
					if (newStatus==1 || newStatus==4){//1 preparation, 4 running
						finished=false;
					}
				}catch(Exception e){
					logger.error("", e);
					finished=false;
				}
			}
		}
		return finished;
	}
	//update CmdStatus for async commands
	//return all the cmd who is not yet finished
	public boolean cmdFinished(CmdStatus cmdStatus){
		boolean finished=true;
		Configuration hconf = HadoopTaskLauncher.getHadoopConf(cconf);
		JobClient jobClient=null;
		try {
			jobClient = new JobClient(hconf);
		}catch(Exception e){
			logger.error("", e);
		}
		
		Map<String, Integer> jobStatusMap = cmdStatus.getJsMap();
		Map<String, Integer> newStatusMap = new HashMap<String, Integer>(); //job id to status
		if (jobStatusMap!=null){
			for (String jid: jobStatusMap.keySet()){
				JobID jobId = JobID.forName(jid);
				try{
					RunningJob rjob = jobClient.getJob(jobId);
					int orgStatus = jobStatusMap.get(jid);
					int newStatus = orgStatus;
					if (rjob!=null){
						newStatus = rjob.getJobStatus().getState().getValue();
						logger.debug(String.format("job %s got status %d", jid, newStatus));
					}else{
						logger.info(String.format("job %s not found in jobClient.", jid));
						//try history server ?
					}
					if (newStatus!=orgStatus){
						newStatusMap.put(jid, newStatus);
					}
					if (newStatus==1 || newStatus==4){//1 preparation, 4 running
						finished=false;
					}
				}catch(Exception e){
					logger.error("error get job status", e.getMessage());
				}
			}
			if (newStatusMap.size()>0){
				for (String jid:newStatusMap.keySet()){
					cmdStatus.getJsMap().put(jid, newStatusMap.get(jid));
				}
				//dsm.addUpdateCrawledItem(cs, null);
				logger.info(String.format("update status for cmd:%s to %s", cmdStatus.getId(), newStatusMap));
			}
		}
		return finished;
	}
	

	//from cmd directly
	public void loadDataFilesDirectly(String param){
		Map<String,String> paramMap = StringUtil.parseMapParams(param);
		int threadNum=10;
		String localDataDir="";
		String include="-";
		String exclude="-";
		if (paramMap.containsKey(LoadDBDataTask.KEY_THREAD_NUM)){
			threadNum = Integer.parseInt(paramMap.get(LoadDBDataTask.KEY_THREAD_NUM));
		}
		if (paramMap.containsKey(LoadDBDataTask.KEY_ROOT_DIR)){
			localDataDir = paramMap.get(LoadDBDataTask.KEY_ROOT_DIR);
		}else{
			logger.error("must has " + LoadDBDataTask.KEY_ROOT_DIR);
			return;
		}
		if (paramMap.containsKey(LoadDBDataTask.KEY_INCLUDE)){
			include = paramMap.get(LoadDBDataTask.KEY_INCLUDE);
		}
		if (paramMap.containsKey(LoadDBDataTask.KEY_EXCLUDE)){
			exclude = paramMap.get(LoadDBDataTask.KEY_EXCLUDE);
		}
		String[] includeArry = new String[]{};
		String[] excludeArr = new String[]{};
		if (!include.equals("") && !include.equals("-")){
			includeArry = include.split(",");
		}
		if (!exclude.equals("") && !exclude.equals("-")){
			excludeArr = exclude.split(",");
		}
		LoadDBDataTask.launch(marketId, marketId, cconf.getBigdbconf(), threadNum, localDataDir, includeArry, excludeArr);
	}
	
	//param is null:all, startwith -:exclude these cmd, startwith +:only include these cmd, else group name
	public List<CmdStatus> runAllCmd(String param) throws InterruptedException {
		logger.info(String.format("runAllCmd with param:%s", param));
		CrawlCmdGroupType groupType = CrawlCmdGroupType.all;
		String[] excludeCmds = null;
		String[] includeCmds = null;
		if (param==null){
			groupType = CrawlCmdGroupType.all;
		}else if (param.equals(CrawlCmdGroupType.nonequote.toString())){
			groupType = CrawlCmdGroupType.nonequote;
		}else if (param.startsWith(EXCLUDE_MARK+"")){
			//means exclude which cmd
			param = param.substring(1);
			excludeCmds = StringUtils.split(param,EXCLUDE_MARK);
			logger.info(String.format("excludeCmds: %s", Arrays.asList(excludeCmds)));
		}else if (param.startsWith(INCLUDE_MARK+"")){
			//means exclude which cmd
			param = param.substring(1);
			includeCmds = StringUtils.split(param,INCLUDE_MARK);
		}
		
		List<CmdStatus> cmdStatusList = new ArrayList<CmdStatus>();
		ETLConfig ec = ETLConfig.getETLConfig(marketId);
		Date lastRunDate = null;
		List<String> curIds = null; 
		List<String> deltaIds = null;
		String strEndDate=sdf.format(endDate);
		
		MarketInfo mi = (MarketInfo) spm.get(MarketInfo.class, new MarketInfoId(marketId, endDate));
		
		if (mi!=null){//has marketInfo for endDate
			curIds = (List<String>) mi.getStockIds();
		}else{
			//get the latest marketInfo before the endDate, suppose there is no marketInfo after endDate
			MarketInfo lmi = spm.getLatestMarketInfo(marketId);
			if (lmi!=null && lmi.getId().getEndDate().after(endDate)){
				logger.warn(String.format("please use endDate after current which is %s", lmi.getId().getEndDate()));
			}else{
				mi = runIdsCmd(ec, marketId, endDate);//this is almost run daily to detect new stocks listed
				curIds = mi.getStockIds();
				List<String> preIds = new ArrayList<String>();
				if (lmi!=null)
					preIds = lmi.getStockIds();
				//get the delta
				deltaIds = new ArrayList<String>();
				deltaIds.addAll(curIds);
				deltaIds.removeAll(preIds);
				//get last run date
				if (startDate!=null){
					lastRunDate = startDate;
				}else{
					if (lmi!=null){
						lastRunDate = lmi.getId().getEndDate();
					}else{
						lastRunDate = ec.getMarketStartDate();
					}
				}
				logger.info(String.format("market %s: %d at %s, %d at %s", marketId, preIds.size(), lastRunDate, curIds.size(), endDate));
				if (deltaIds.size()>0){
					//run static crawl cmds for delta market
					cmdStatusList.addAll(runCmdGroup(marketId, deltaIds, lastRunDate, endDate, groupType, 
							CrawlCmdType.nondynamic, includeCmds, excludeCmds));
					//store the static crawl cmd results into meta data store
				}
			}
		}
		logger.info(String.format("run dynamic cmd for market %s from %s to %s", marketId, lastRunDate, endDate));
		cmdStatusList.addAll(runCmdGroup(marketId, curIds, lastRunDate, endDate, groupType, 
				CrawlCmdType.dynamic, includeCmds, excludeCmds)); //dynamic part
		return cmdStatusList;
	}
	
	//param is null:all, startwith -:exclude these cmd, startwith +:only include these cmd
	public String[] postprocess(String param){
		ETLConfig sc = ETLConfig.getETLConfig(marketId);
		String[] excludeCmds = null;
		String[] includeCmds = null;
		if (param!=null){
			if (param.startsWith(EXCLUDE_MARK+"")){
				param = param.substring(1);
				excludeCmds = StringUtils.split(param,EXCLUDE_MARK);
			}else if (param.startsWith(INCLUDE_MARK+"")){
				param = param.substring(1);
				includeCmds = StringUtils.split(param,INCLUDE_MARK);
			}
		}
		List<String> jobIds = new ArrayList<String>();
		String datePart = sc.getDatePart(marketId, startDate, endDate);
		Map<LaunchableTask, String[]> ppMap = sc.getPostProcessMap();
		for (LaunchableTask t:ppMap.keySet()){
			String[] cmds = ppMap.get(t);
			for (String cmd:cmds){
				if (includeCmds!=null){
					if (!Arrays.asList(includeCmds).contains(cmd)){
						continue;
					}
				}
				if (excludeCmds!=null){
					if (Arrays.asList(excludeCmds).contains(cmd)){
						continue;
					}
				}
				String[] jobIdsOneCmd = t.launch(this.propFile, marketId, cconf, datePart, new String[]{cmd});
				if (jobIdsOneCmd!=null){
					jobIds.addAll(Arrays.asList(jobIdsOneCmd));
				}
			}
		}
		String[] rt = new String[jobIds.size()];
		return jobIds.toArray(rt);
	}
	
	//param is null:all, startwith -:exclude these cmd, startwith +:only include these cmd
	public String[] run_merge(String param){
		ETLConfig sc = ETLConfig.getETLConfig(marketId);
		String datePart = sc.getDatePart(marketId, startDate, endDate);
		return MergeTask.launch(sc, this.propFile, this.marketId, cconf, datePart, param, true);
	}
	
	public static String dumpDir = "/data/cydata/stock/merge/";
	//param is null:all, startwith -:exclude these cmd, startwith +:only include these cmd
	public void dumpFiles(String param){
		ETLConfig sc = ETLConfig.getETLConfig(marketId);
		String[] excludeCmds = new String[]{};
		String[] includeCmds = null;
		if (param!=null){
			if (param.startsWith(EXCLUDE_MARK+"")){
				param = param.substring(1);
				excludeCmds = StringUtils.split(param,EXCLUDE_MARK);
			}else if (param.startsWith(INCLUDE_MARK+"")){
				param = param.substring(1);
				includeCmds = StringUtils.split(param,INCLUDE_MARK);
			}
		}
		String strEndDate = sdf.format(endDate);
		String localDirRoot = dumpDir + strEndDate;
		String[] includeFolders = null;
		if (includeCmds!=null){
			includeFolders = new String[includeCmds.length];
			for (int i=0;i<includeFolders.length;i++){
				includeFolders[i] = String.format("%s/%s_%s", includeCmds[i], marketId, strEndDate);
			}
		}else{
			includeFolders = new String[]{String.format("%s_%s", marketId, strEndDate)};
		}
		TransferHdfsFile.launch(20, cconf.getHdfsDefaultName(), "/reminder/items/merge",
				localDirRoot, includeFolders, ListUtil.concatAll(sc.getSlowCmds(), excludeCmds), false);
	}
	
	//param is null:all, startwith -:exclude these cmd, startwith +:only include these cmd
	public void loadDataFiles(String param){
		String[] excludeCmds = new String[]{};
		String[] includeCmds = null;
		if (param!=null){
			if (param.startsWith(EXCLUDE_MARK+"")){
				param = param.substring(1);
				excludeCmds = StringUtils.split(param,EXCLUDE_MARK);
			}else if (param.startsWith(INCLUDE_MARK+"")){
				param = param.substring(1);
				includeCmds = StringUtils.split(param,INCLUDE_MARK);
			}
		}
		String strEndDate = sdf.format(endDate);
		String localDirRoot = dumpDir + strEndDate;
		String[] includeFolders = new String[]{this.marketId};
		if (includeCmds!=null){
			includeFolders = includeCmds;
		}
		LoadDBDataTask.launch(marketId, strEndDate, cconf.getBigdbconf(), 20, localDirRoot, includeFolders, excludeCmds);
	}
	
	public void postImport(){
		ETLConfig sc = ETLConfig.getETLConfig(marketId);
		String postImportFileName = sc.postImportSql();
		try{
			if (postImportFileName!=null){
				InputStreamReader br = new InputStreamReader(new FileInputStream(new File(String.format("/data/reminder/sql/%s", postImportFileName))), "UTF-8");
				Connection con = SqlUtil.getConnection(this.getCconf().getBigdbconf());
				ScriptRunner sr = new ScriptRunner(con, false, false);
				sr.runScript(br);
				SqlUtil.closeResources(con, null);
			}
		}catch(Exception e){
			logger.error("", e);
			return;
		}
	}
	
	//remove the raw files for this cmd under folders from startDate to endDate
	public void removeRaw(String cmd){
		if (startDate == null || endDate==null){
			logger.error("for removeRaw startDate and endDate can't be null.");
		}
		Date d = startDate;
		try {
			FileSystem fs = FileSystem.get(HadoopTaskLauncher.getHadoopConf(cconf));
			while (d.before(endDate)){
				String fn = String.format("/reminder/items/raw/%s_%s/%s", this.marketId, sdf.format(d), cmd);
				Path p = new Path(fn);
				if (fs.exists(p)){
					fs.delete(p, true);
					logger.info(String.format("delete path %s", fn));
				}
				d = DateTimeUtil.tomorrow(d);
			}
		}catch(Exception e){
			logger.error("", e);
		}
	}
	
	private static final int poll_interval=20000;
	public static final String CMD_NAME="cmd";
	public static final String STEP_NAMES="step";
	public static String[] cmds = new String[]{"runAllCmd", "postprocess", "run_merge", "dumpFiles", "loadDataFiles", "postImport"};
	public void updateAll(String inparam) throws InterruptedException{
		Map<String,String> params = StringUtil.parseMapParams(inparam);
		String cmdParam = params.get(CMD_NAME);
		String stepParam = params.get(STEP_NAMES);
		logger.info(String.format("cmd:%s,step:%s", cmdParam, stepParam));
		String[] cmdParams = new String[]{cmdParam, cmdParam, cmdParam, cmdParam, cmdParam, null};
		if (inparam !=null){
			if (CrawlCmdGroupType.nonequote.toString().equals(cmdParam)){//for none trading days
				cmds = new String[]{"runAllCmd", "run_merge", "dumpFiles", "loadDataFiles", "postImport"};
				cmdParams = new String[]{cmdParam, null, null, null, null};
			}
		}
		int step =0;
		boolean oneStep = false;
		if (stepParam!=null){
			if (!stepParam.contains(".")){//for just 1 step
				step = Integer.parseInt(stepParam);
			}else{
				stepParam = stepParam.substring(0, stepParam.indexOf("."));
				step = Integer.parseInt(stepParam);
				oneStep = true;
			}
		}
		for (int i=step; i<cmds.length; i++){
			List<CmdStatus> csl = this.runSpecial(cmds[i], cmdParams[i]);
			if (csl!=null){//async
				if (csl.size()==0){
					//cmd failed, stop all
					logger.error(String.format("cmd %s with param %s failed, stop all.", cmds[i], cmdParams[i]));
					return;
				}
				int allDone=0;
				while(allDone<csl.size()){
					allDone = 0;
					for(CmdStatus cs:csl){
						if (cs!=null){
							boolean done = this.cmdFinished(cs);
							if (!done){
								Thread.sleep(poll_interval);
								break;
							}else{
								allDone++;
							}
						}else{
							logger.error(String.format("cmd %s with param %s return cmd status null.", cmds[i], cmdParams[i]));
							allDone++;
						}
					}
				}
			}
			if (oneStep){
				return;
			}
		}
	}

	public List<CmdStatus> runSpecial(String method, String param){
		ETLConfig sc = ETLConfig.getETLConfig(marketId);
		Method m;
		try {
			boolean noParam=true;
			if (param==null){
				//try method without param 1st
				try{
					m = getClass().getMethod(method);
				}catch(Exception e){
					m = getClass().getMethod(method, String.class);
					noParam=false;
				}
			}else{
				m = getClass().getMethod(method, String.class);
				noParam=false;
			}
			logger.info(String.format("going to run cmd %s with ed:%s, sd:%s, marketId:%s", method, endDate, startDate, marketId));
			if (m.getReturnType() == String[].class){
				String[] jobIds = null;
				if (noParam){
					jobIds = (String[]) m.invoke(this);
				}else{
					jobIds = (String[]) m.invoke(this, param);
				}
				if (jobIds!=null){
					CmdStatus cs = new CmdStatus(marketId, method, endDate);
					for (String jobId:jobIds){
						if (jobId!=null){
							cs.getJsMap().put(jobId, 4);//PREPARE
						}
					}
					//dsm.addUpdateCrawledItem(cs, null);
					List<CmdStatus> cmsl = new ArrayList<CmdStatus>();
					cmsl.add(cs);
					return cmsl;
				}
			}else if (m.getReturnType() == List.class){//List<CmdStatus>
				if (noParam){
					return (List<CmdStatus>)m.invoke(this);
				}else{
					return (List<CmdStatus>)m.invoke(this, param);
				}
			}else{
				if (noParam){
					m.invoke(this);
				}else{
					m.invoke(this, param);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	//getter, setter
	public CrawlConf getCconf(){
		return this.cconf;
	}
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public String getSpecialParam() {
		return specialParam;
	}
	public void setSpecialParam(String specialParam) {
		this.specialParam = specialParam;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
