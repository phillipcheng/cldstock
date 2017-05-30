package org.cldutil.stock.trade.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.cldutil.stock.common.StockConfig;
import org.cldutil.stock.common.StockUtil;
import org.cldutil.stock.trade.AutoTrader;
import org.cldutil.stock.trade.HistoryDumpMgr;
import org.cldutil.stock.trade.StreamHandler;
import org.cldutil.stock.trade.TradeDataMgr;
import org.junit.Test;

public class TestHistoryDump {
	@Test
	public void test1() throws Exception {
		AutoTrader at = new AutoTrader();
		StockConfig sc = StockUtil.getStockConfig(at.getBaseMarketId());
		TradeDataMgr tdm = new TradeDataMgr(at, sc);
		HistoryDumpMgr hdm = new HistoryDumpMgr(at.getHistoryDumpProperties(), tdm, sc);
		new Thread(hdm).start();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("C:\\mydoc\\myprojects\\ereminder\\Reminder\\modules\\trade\\input\\input2.csv")));
		String line = null;
		while ((line=br.readLine())!=null){
			StreamHandler.processCsvData("AAPL", line, tdm);
		}
		br.close();
		
		Thread.sleep(30000);//30 seconds
	}
}
