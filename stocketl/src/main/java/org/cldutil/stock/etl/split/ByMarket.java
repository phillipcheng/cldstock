package org.cldutil.stock.etl.split;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.datacrawl.CrawlConf;
import org.cldutil.stock.etl.ETLUtil;
import org.cldutil.stock.etl.base.ETLConfig;
import org.cldutil.taskmgr.entity.Task;

public class ByMarket extends SplitTask{
	private static Logger logger =  LogManager.getLogger(ByMarket.class);
	
	public ByMarket(ETLConfig ec, CrawlConf cconf, String propfile, Task t,  String cmd, Map<String, Object> params){
		super(ec, cconf, propfile, t, cmd, params);
	}

	@Override
	public List<Task> getTasks() {
		logger.info("into runTaskByMarket");
		List<Task> tlist = new ArrayList<Task>();
		t.putAllParams(params);
		tlist.add(t);
		return tlist;
	}

	@Override
	public String getTaskName() {
		t.putAllParams(params);
		return ETLUtil.getTaskName(ec, cmd, t.getParamMap());
	}
}
