package org.cldutil.stock.trade.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.stock.strategy.IntervalUnit;
import org.cldutil.stock.strategy.SelectCandidateResult;
import org.cldutil.stock.strategy.StockOrder;
import org.cldutil.stock.strategy.TradeStrategy;
import org.cldutil.stock.trade.AutoTrader;
import org.cldutil.stock.trade.StockOrderType;
import org.cldutil.stock.trade.persist.StockPosition;
import org.cldutil.stock.trade.persist.TradePersistMgr;
import org.junit.Before;
import org.junit.Test;

public class TestTradePersistMgr {
	
	private static Logger logger =  LogManager.getLogger(TestTradePersistMgr.class);
	
	AutoTrader at = new AutoTrader();
	
	@Before
	public void setUp(){	
	}
	
	@Test
	public void testAddPosition(){
		String symbol = "AAPL";
		SelectCandidateResult scr = new SelectCandidateResult(symbol, new Date(), 0, 110);
		List<TradeStrategy> tsl = at.getTsl(symbol, IntervalUnit.tick);
		TradeStrategy ts = tsl.get(0);
		Map<String, StockOrder> somap = AutoTrader.genStockOrderMap(scr, ts.getSs(), at.getUseAmount());
		StockOrder buyOrder = somap.get(StockOrderType.buy.name());
		String buyOrderId = buyOrder.getOrderId();
		StockPosition sp = new StockPosition(scr, ts.getBs().getName(), somap, buyOrderId);
		TradePersistMgr.createStockPosition(at.getDbConf(), sp);
	}
	
	@Test
	public void getPosition(){
		String buyOrderId = "20151217044228213_1";
		StockPosition sp = TradePersistMgr.getStockPositionByOrderId(at.getDbConf(), buyOrderId);
		logger.info(sp);
	}
	
	@Test
	public void updatePosition(){
		String buyOrderId = "20151217044228213_1";
		StockPosition sp = TradePersistMgr.getStockPositionByOrderId(at.getDbConf(), buyOrderId);
		Map<String, StockOrder> map = sp.getSoMap();
		StockOrder stopsell = map.get(StockOrderType.sellstop.name());
		StockOrder limitsell = map.get(StockOrderType.selllimit.name());
		TradePersistMgr.updateStopSellOrderId(at.getDbConf(), buyOrderId, stopsell.getOrderId());
		TradePersistMgr.updateLimitSellOrderId(at.getDbConf(), buyOrderId, limitsell.getOrderId());
	}
}
