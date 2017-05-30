package org.cldutil.stock.etl.base;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.cldutil.taskmgr.hadoop.HadoopTaskLauncher;
import org.cldutil.stock.common.StockUtil;
import org.cldutil.stock.etl.StockBase;
import org.cldutil.stock.etl.task.GenNdLable;
import org.cldutil.stock.etl.task.sina.SplitByStockMapper;
import org.cldutil.stock.etl.task.sina.SplitByStockReducer;
import org.cldutil.stock.etl.task.sina.TradeDetailCheckDownload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SinaStockBase extends StockBase{
	protected static Logger logger =  LogManager.getLogger(SinaStockBase.class);
	
	public SinaStockBase(String propFile, String marketId, Date sd, Date ed){
		super(propFile, StockUtil.SINA_STOCK_BASE, marketId, sd, ed, StockUtil.SINA_STOCK_BASE);
	}

	public String[] run_tradedetail_checkdownload(){
		String datePart;
		String strEndDate = sdf.format(this.endDate);
		if (this.startDate==null){
			datePart = SinaETLConfig.HS_A_FIRST_DATE_DETAIL_TRADE + "_" + strEndDate;
		}else{
			datePart = sdf.format(this.startDate) + "_" + strEndDate;
		}
		return new String[]{TradeDetailCheckDownload.launch(cconf, datePart)};
	}
	
	//sina-stock-market-fq
	public String[] splitByStock(){
		Map<String, String> hadoopParams = new HashMap<String, String>();
		HadoopTaskLauncher.updateHadoopMemParams(3072, hadoopParams);
		return new String[] {
				HadoopTaskLauncher.hadoopExecuteTasks(getCconf(), hadoopParams, 
						new String[]{"/reminder/items/merge/"+this.specialParam}, 
						true, "/reminder/items/mlinput/"+this.specialParam, false, 
						SplitByStockMapper.class, 
						SplitByStockReducer.class, false)};
	}
	
	public String[] genNdLable(){
		return GenNdLable.launch(this.propFile, getCconf(), this.specialParam, true);
	}

	@Override
	public boolean fqReady(Date today) {
		//check fq
		return true;
	}
}
