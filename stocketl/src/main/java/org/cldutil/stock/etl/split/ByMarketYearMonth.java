package org.cldutil.stock.etl.split;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.datacrawl.CrawlConf;
import org.cldutil.stock.etl.ETLUtil;
import org.cldutil.stock.etl.base.ETLConfig;
import org.cldutil.taskmgr.entity.Task;

public class ByMarketYearMonth extends SplitTask{
	private static Logger logger =  LogManager.getLogger(ByMarketYearMonth.class);
	
	private Date sd;
	private Date ed;
	
	public ByMarketYearMonth(ETLConfig ec, CrawlConf cconf, String propfile, Task t,  String cmd, Map<String, Object> params,
			Date sd, Date ed){
		super(ec, cconf, propfile, t, cmd, params);
		this.sd = sd;
		this.ed = ed;
	}

	@Override
	public List<Task> getTasks() {
		logger.info("into runTaskByMarketYearMonth");
		List<Task> tlist = new ArrayList<Task>();
		//
		Calendar calInst = Calendar.getInstance();
		calInst.setTime(ed);
		int cy = calInst.get(Calendar.YEAR);
		int cm = calInst.get(Calendar.MONTH);
		if (sd==null){
			sd = ec.getMarketStartDate();
		}
		calInst.setTime(sd);
		int fy = calInst.get(Calendar.YEAR);
		int fm = calInst.get(Calendar.MONTH);
		for (int year=fy; year<=cy; year++){
			int sm = 1;
			int tm = 12;
			if (year==fy){
				sm = fm;
			}else if (year==cy){
				tm = cm;
			}
			for (int month=sm; month<=tm; month++){
				if (year==cy && month>cm){
					break;
				}
				String strMonth = month + "";
				if (month<10){
					strMonth = "0" + month;
				}
				Task t1 = t.clone(ETLUtil.class.getClassLoader());
				t1.putParam("year", year);
				t1.putParam("month", strMonth);
				t1.putAllParams(params);
				tlist.add(t1);
			}
		}
		return tlist;
	}

	@Override
	public String getTaskName() {
		return ETLUtil.getTaskName(ec, cmd, null);
	}
}
