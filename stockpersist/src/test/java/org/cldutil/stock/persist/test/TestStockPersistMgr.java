package org.cldutil.stock.persist.test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.cldutil.stock.persistence.CmdStatus;
import org.cldutil.stock.persistence.MarketInfo;
import org.cldutil.stock.persistence.MarketInfoId;
import org.cldutil.stock.persistence.StockPersistMgr;
import org.junit.Test;

public class TestStockPersistMgr {
	
	@Test
	public void testAddMarketInfo(){
		StockPersistMgr spm = new StockPersistMgr();
		spm.init();
		
		MarketInfo mi = new MarketInfo();
		String marketId="test1";
		Date d = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
		MarketInfoId miid = new MarketInfoId(marketId, d);
		mi.setId(miid);
		List<String> stockIds = Arrays.asList(new String[]{"AA","BB"});
		mi.setStockIds(stockIds);
		spm.addOrUpdate(mi);
		spm.cleanup();
	}
	
	@Test
	public void testUpdateMarketInfo(){
		StockPersistMgr spm = new StockPersistMgr();
		spm.init();
		
		MarketInfo mi = new MarketInfo();
		String marketId="test1";
		Date d = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
		MarketInfoId miid = new MarketInfoId(marketId, d);
		mi.setId(miid);
		List<String> stockIds = Arrays.asList(new String[]{"AA","BB","CC"});
		mi.setStockIds(stockIds);
		spm.addOrUpdate(mi);
		spm.cleanup();
	}
	
	@Test
	public void testAddCmdStatus(){
		StockPersistMgr spm = new StockPersistMgr();
		spm.init();
		
		String marketId="test1";
		String cmdName="cmd1";
		Date d = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
		CmdStatus cs = new CmdStatus(marketId, cmdName, d);
		Map<String,Integer> jsMap = new HashMap<String,Integer>();
		String jobId1="jobId1";
		jsMap.put(jobId1, 1);
		cs.setJsMap(jsMap);
		
		spm.addOrUpdate(cs);
		
		spm.cleanup();
	}
	
	@Test
	public void testUpdateCmdStatus(){
		StockPersistMgr spm = new StockPersistMgr();
		spm.init();
		
		String marketId="test1";
		String cmdName="cmd1";
		Date d = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
		CmdStatus cs = new CmdStatus(marketId, cmdName, d);
		Map<String,Integer> jsMap = new HashMap<String,Integer>();
		jsMap.put("jobId1", 1);
		jsMap.put("jobId2", 2);
		cs.setJsMap(jsMap);
		
		spm.addOrUpdate(cs);
		
		spm.cleanup();
	}

}
