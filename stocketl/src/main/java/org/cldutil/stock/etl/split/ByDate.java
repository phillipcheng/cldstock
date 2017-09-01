package org.cldutil.stock.etl.split;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.datacrawl.CrawlConf;
import org.cldutil.etl.fci.AbstractCrawlItemToCSV;
import org.cldutil.stock.common.StockConfig;
import org.cldutil.stock.common.StockUtil;
import org.cldutil.stock.etl.ETLUtil;
import org.cldutil.stock.etl.base.ETLConfig;
import org.cldutil.stock.persistence.StockPersistMgr;
import org.cldutil.taskmgr.entity.Task;
import org.cldutil.util.DateTimeUtil;

public class ByDate extends SplitTask{
	private static Logger logger =  LogManager.getLogger(ByDate.class);
	
	private Date startDate;
	private Date endDate;
	
	public ByDate(ETLConfig ec, CrawlConf cconf, String propfile, Task t,  String cmd, Map<String, Object> params,
			Date sd, Date ed){
		super(ec, cconf, propfile, t, cmd, params);
		this.startDate = sd;
		this.endDate = ed;
	}

	@Override
	public List<Task> getTasks() {
		logger.info("into runTaskByDate");
		StockConfig sc = StockUtil.getStockConfig(ec.getMarketId());
		Map<String, String> tables = ec.getTablesByCmd(cmd);
		Date luDate = null;
		if (tables!=null)
			luDate = StockPersistMgr.getMarketLUDateByCmd(tables.keySet(), cconf.getBigdbconf());
		Date sd = null;
		if (luDate!=null){
			sd = DateTimeUtil.tomorrow(luDate);
		}else if (startDate!=null){
			sd = startDate;
		}else{
			String strSd = ec.getStartDate(cmd);
			if (strSd!=null){
				try {
					sd = ec.getSdf().parse(strSd);
				} catch (ParseException e) {
					logger.error("", e);
				}
			}else{
				sd = ec.getMarketStartDate();
			}
		}
		LinkedList<Date> dll = StockUtil.getOpenDayList(sd, endDate, sc.getHolidays());
		List<Task> tlist = new ArrayList<Task>();
		Iterator<Date> tryDates = dll.listIterator();
		while(tryDates.hasNext()){
			Date d = tryDates.next();
			String dstr = ec.getSdf().format(d);
			Task t1 = t.clone(ETLUtil.class.getClassLoader());
			if (params!=null){
				t1.putAllParams(params);
			}
			t1.putParam("date", dstr);
			tlist.add(t1);
		}
		return tlist;
	}

	@Override
	public String getTaskName() {
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(AbstractCrawlItemToCSV.FN_STARTDATE, startDate);
		if (params!=null)
			taskParams.putAll(params);
		return ETLUtil.getTaskName(ec, cmd, taskParams);
	}

}
