package org.cldutil.stock.etl.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.cldutil.stock.common.StockConfig;
import org.cldutil.stock.common.StockUtil;
import org.cldutil.stock.config.HKStockConfig;
import org.cldutil.stock.etl.StockBase;

public class HKStockBase extends StockBase{
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public HKStockBase(String propFile, String marketId, Date sd, Date ed){
		super(propFile, StockUtil.HK_STOCK_BASE, marketId, sd, ed, StockUtil.HK_STOCK_BASE);
	}

	@Override
	public boolean fqReady(Date today) {
		return false;
	}
}
