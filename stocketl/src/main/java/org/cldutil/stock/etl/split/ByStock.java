package org.cldutil.stock.etl.split;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.datacrawl.CrawlConf;
import org.cldutil.etl.fci.AbstractCrawlItemToCSV;
import org.cldutil.stock.etl.ETLUtil;
import org.cldutil.stock.etl.base.ETLConfig;
import org.cldutil.stock.persistence.StockPersistMgr;
import org.cldutil.taskmgr.entity.Task;
import org.cldutil.util.DateTimeUtil;

//holding-insiders, dividend-history, quote-historical, short-interest, corp-info, nasdaq-fq, sina-bulletin
public class ByStock extends SplitTask{
	private static Logger logger =  LogManager.getLogger(ByStock.class);
	
	private String marketId;
	private List<String> stockids;
	
	public ByStock(ETLConfig ec, CrawlConf cconf, String propfile, Task t,  String cmd, Map<String, Object> params, 
			String marketId, List<String> stockids){
		super(ec, cconf, propfile, t, cmd, params);
		this.marketId = marketId;
		this.stockids = stockids;
	}

	@Override
	public List<Task> getTasks() {
		logger.info("into runTaskByStock");
		Map<String, Date> stockLUMap = new HashMap<String, Date>();
		if (!Arrays.asList(ec.getUpdateAllCmds()).contains(cmd)){
			if (ETLUtil.needCheckDB(ec, cconf, cmd)){
				String query = ec.getStockLUDateSqlByCmd(cmd);
				stockLUMap = StockPersistMgr.getStockLUDateByCmd(query, cconf.getBigdbconf());
			}
		}
		List<Task> tlist = new ArrayList<Task>();
		for (String stockid: stockids){
			Date sd = null;
			if (stockLUMap.containsKey(stockid)){
				sd = stockLUMap.get(stockid);//date in the db is loaded from text files which are displayed on webpage which is in each market's tz
				if (sd!=null) {
					sd = DateTimeUtil.tomorrow(sd);
				}
			}
			if (!Arrays.asList(ec.getFirstStartTimeUseNullCmds()).contains(cmd)){
				if (sd==null){
					//need to open url for each year and quarter, so we need to use ipo start date.
					sd = ETLUtil.getIPODateByStockId(ec, marketId, stockid, cmd);
				}
				if (sd==null){
					sd = ec.getMarketStartDate();
				}
			}else{
				//keep sd null
			}
			Task t1 = t.clone(ETLUtil.class.getClassLoader());
			if (params!=null){
				t1.putAllParams(params);
				if (sd!=null){
					t1.putParam(AbstractCrawlItemToCSV.FN_STARTDATE, ec.getSdf().format(sd));
				}else{//set sd null
					t1.putParam(AbstractCrawlItemToCSV.FN_STARTDATE, null);
				}
			}
			t1.putParam("stockid", stockid);
			tlist.add(t1);
		}
		return tlist;
	}

	@Override
	public String getTaskName() {
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(AbstractCrawlItemToCSV.FN_MARKETID, marketId);
		if (params!=null)
			taskParams.putAll(params);
		String taskName = ETLUtil.getTaskName(ec, cmd, taskParams);
		return taskName;
	}
}
