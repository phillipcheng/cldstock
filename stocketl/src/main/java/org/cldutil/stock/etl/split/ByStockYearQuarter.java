package org.cldutil.stock.etl.split;

import java.util.ArrayList;
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
import org.cldutil.xml.mytaskdef.ConfKey;


public class ByStockYearQuarter extends SplitTask{
	private static Logger logger =  LogManager.getLogger(ByStockYearQuarter.class);
	
	private String marketId;
	private List<String> stockids;
	private Date sd;
	private Date endDate;
	
	public ByStockYearQuarter(ETLConfig ec, CrawlConf cconf, String propfile, Task t,  String cmd, Map<String, Object> params, 
			String marketId, List<String> stockids, Date sd, Date ed){
		super(ec, cconf, propfile, t, cmd, params);
		
		this.marketId = marketId;
		this.stockids = stockids;
		this.sd = sd;
		this.endDate = ed;
	}
	

	private static int[] moveAhead(int[] yq, int step){
		int[] retYQ = new int[2];
		if (step==0){
			return yq;
		}else if (step==1){
			if (yq[1]>1){
				retYQ[1] = yq[1]-1;
				retYQ[0] = yq[0];
			}else{
				retYQ[1]= 4;
				retYQ[0]= yq[0]-1;
			}
			return retYQ;
		}else{
			logger.error("lookahead value not supported:" + step);
			return retYQ;
		}
	}
	enum OpenUrlType{
		byYearAndQuarter,
		byYear,
		byIdOnly
	}
	
	public List<Task> getTasks(){
		List<Task> tlist = new ArrayList<Task>();
		Map<String, Date> stockLUMap = new HashMap<String, Date>();
		if (ETLUtil.needCheckDB(ec, cconf, cmd)){
			String query = ec.getStockLUDateSqlByCmd(cmd);
			stockLUMap = StockPersistMgr.getStockLUDateByCmd(query, cconf.getBigdbconf());
		}
		int[] eyq = DateTimeUtil.getYearQuarter(endDate);
		int endYear = eyq[0];
		int endQuarter = eyq[1];
		int lookahead = ETLUtil.getLookahead(t); //how many quarter to look ahead of the current one
		//move one quarter forward
		int[] retYQ = moveAhead(new int[]{endYear, endQuarter}, lookahead);
		endYear = retYQ[0];
		endQuarter = retYQ[1];
		
		OpenUrlType out;
		String startUrl = ETLUtil.getStartUrl(t);
		if (startUrl.contains(ConfKey.PARAM_PRE+AbstractCrawlItemToCSV.FN_YEAR+ConfKey.PARAM_POST) && 
				startUrl.contains(ConfKey.PARAM_PRE+AbstractCrawlItemToCSV.FN_QUARTER+ConfKey.PARAM_POST)){
			//need to open url for each year and quarter, so we need to use ipo start date.
			out = OpenUrlType.byYearAndQuarter;
		}else if (startUrl.contains(ConfKey.PARAM_PRE+AbstractCrawlItemToCSV.FN_YEAR+ConfKey.PARAM_POST)){
			out = OpenUrlType.byYear;
		}else{
			out = OpenUrlType.byIdOnly;
		}
		
		for (String stockid: stockids){
			Date sd = null;
			if (stockLUMap.containsKey(stockid)){
				sd = stockLUMap.get(stockid);
				if (sd!=null){//since we crawl [startDate, endDate)
					sd = DateTimeUtil.tomorrow(sd);
				}
			}
			if (sd==null){
				//need to open url for each year and quarter, so we need to use ipo start date.
				sd = ETLUtil.getIPODateByStockId(ec, marketId, stockid, cmd);
			}
			int[] yq = DateTimeUtil.getYearQuarter(sd);
			int startYear = yq[0];
			int startQuarter = yq[1];
			String strSd = ec.getSdf().format(sd);
			if (startYear<endYear||
					(startYear==endYear&&startQuarter<=endQuarter)){//we have quarter need to work on
				if (out==OpenUrlType.byYearAndQuarter){
					int year;
					int quarter;
					for (year=startYear; year<=endYear; year++){
						int startQ;
						int endQ;
						if (startYear==endYear){//same year
							startQ=startQuarter;
							endQ =endQuarter;
						}else{
							if (year==startYear){//1st year
								startQ = startQuarter;
								endQ=4;
							}else if (year==endYear){//last year
								startQ=1;
								endQ=endQuarter;
							}else{//for any year between
								startQ=1;
								endQ=4;
							}
						}
						for (quarter=startQ;quarter<=endQ;quarter++){
							Task t1 = t.clone(ETLUtil.class.getClassLoader());
							t1.putParam("stockid", stockid);
							t1.putParam("year", year);
							t1.putParam("quarter", quarter);
							t1.putAllParams(params);
							t1.putParam(AbstractCrawlItemToCSV.FN_STARTDATE, strSd);//overwrite startDate
							tlist.add(t1);
						}
					}
				}else if (out==OpenUrlType.byYear){
					int year;
					for (year=startYear; year<=endYear; year++){
						Task t1 = t.clone(ETLUtil.class.getClassLoader());
						t1.putParam("stockid", stockid);
						t1.putParam("year", year);
						if (year==startYear){//for the start year, the there is a startQuarter
							t1.putParam("quarter", startQuarter);
						}else{
							t1.putParam("quarter", 0);//any quarter
						}
						t1.putAllParams(params);
						t1.putParam(AbstractCrawlItemToCSV.FN_STARTDATE, strSd);//overwrite startDate
						tlist.add(t1);
						
					}
				}else if (out==OpenUrlType.byIdOnly){//set year and quarter to startYear and startQuarter
					Task t1 = t.clone(ETLUtil.class.getClassLoader());
					t1.putParam("stockid", stockid);
					t1.putParam("year", startYear);
					t1.putParam("quarter", startQuarter);
					t1.putAllParams(params);
					t1.putParam(AbstractCrawlItemToCSV.FN_STARTDATE, strSd);//overwrite startDate
					tlist.add(t1);
				}
			}
		}
		return tlist;
	}
	
	public String getTaskName(){
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(AbstractCrawlItemToCSV.FN_MARKETID, marketId);
		String taskName = ETLUtil.getTaskName(ec, cmd, taskParams);
		return taskName;
	}
}
