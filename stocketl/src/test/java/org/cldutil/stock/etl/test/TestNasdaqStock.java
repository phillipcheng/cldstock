package org.cldutil.stock.etl.test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.util.jdbc.SqlUtil;
import org.cldutil.stock.common.StockConfig;
import org.cldutil.stock.common.StockUtil;
import org.cldutil.stock.etl.StockBase;
import org.cldutil.stock.etl.base.ETLConfig;
import org.cldutil.stock.etl.base.NasdaqETLConfig;
import org.cldutil.stock.etl.base.NasdaqStockBase;
import org.cldutil.stock.mapper.ext.NasdaqSplitJDBCMapper;
import org.cldutil.stock.mapper.ext.NasdaqUpcomingDivMapper;
import org.junit.Before;
import org.junit.Test;

public class TestNasdaqStock {
	private static Logger logger =  LogManager.getLogger(TestNasdaqStock.class);
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static String START_DATE = "2016-01-01";
	private static String END_DATE = "2017-08-01";
	private static String SMALL_END_DATE="2001-01-01";
	
	private static Date startDate=null;
	private static Date endDate = null;
	static{
		try{
			//sdf.setTimeZone(TimeZone.getTimeZone("EST"));
			startDate = sdf.parse(START_DATE);
			endDate = sdf.parse(END_DATE);
		}catch(Exception e){
			logger.error("", e);
		}
	}
	
	private String marketId = NasdaqETLConfig.MarketId_NASDAQ_Test;
	private String propFile = "client1-v2.properties";
	
	private NasdaqStockBase nsb;
	private StockConfig sc;
	private ETLConfig ec;

	public TestNasdaqStock(){
		super();
	}
	
	@Before
	public void setUp()throws Exception{
		nsb = new NasdaqStockBase(propFile, marketId, startDate, endDate);
		sc = StockUtil.getStockConfig(StockUtil.NASDAQ_STOCK_BASE);
		ec = ETLConfig.getETLConfig(StockUtil.NASDAQ_STOCK_BASE);
	}
	
	//static commands
	@Test
	public void testInitTestMarket() throws Exception{
		nsb.runIdsCmd(ec, NasdaqETLConfig.MarketId_NASDAQ_Test, sdf.parse(END_DATE));
		nsb.runIdsCmd(ec, NasdaqETLConfig.MarketId_NASDAQ_Test, sdf.parse(SMALL_END_DATE));
	}
	
	@Test
	public void testIdsCmd() throws Exception{
		nsb.runIdsCmd(ec, StockUtil.NASDAQ_STOCK_BASE, sdf.parse(END_DATE));
	}
	
	@Test
	public void testIPO() throws Exception{
		nsb.runCmd(NasdaqETLConfig.STOCK_IPO, marketId, null, END_DATE);
	}
	//by stock cmds
	@Test
	public void testCmd_QuoteHistory(){
		nsb.runCmd(NasdaqETLConfig.QUOTE_HISTORY, marketId, null, END_DATE);
	}
	@Test
	public void testPostProcess() throws Exception{
		nsb.setEndDate(sdf.parse("2015-10-09"));
		nsb.postprocess(null);
	}
	//by stock and daily
	@Test
	public void testCmd_QuoteTick(){
		nsb.runCmd(NasdaqETLConfig.QUOTE_TICK, marketId, null, null);
	}
	@Test
	public void testCmd_QuotePreMarket(){
		String strD = sdf.format(sc.getLatestOpenMarketDate(new Date()));
		nsb.runCmd(NasdaqETLConfig.QUOTE_PREMARKET, marketId, strD, strD);
	}
	@Test
	public void testCmd_QuoteAfterHours(){
		nsb.runCmd(NasdaqETLConfig.QUOTE_AFTERHOURS, marketId, null, null);
	}
	//by stock and quarterly
	@Test
	public void testCmd_HolderSummary(){
		nsb.runCmd(NasdaqETLConfig.HOLDING_SUMMARY, marketId, null, sdf.format(sc.getLatestOpenMarketDate(new Date())));
		nsb.runCmd(NasdaqETLConfig.HOLDING_TOP5, marketId, null, sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_EarnAnnounce(){
		nsb.runCmd(NasdaqETLConfig.EARN_ANNOUNCE, marketId, "2017-08-03", "2017-08-04");
	}
	@Test
	public void testCmd_EarnAnnounceTime(){
		nsb.runCmd(NasdaqETLConfig.EARN_ANNOUNCE_TIME, marketId, "2017-08-03", "2017-08-04");
	}
	@Test
	public void testCmd_Fr_QuarterlyIncomeStatement(){
		nsb.runCmd(NasdaqETLConfig.INCOME_STATEMENT, marketId, null, sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_Fr_QuarterlyIncomeStatementOneQ(){
		nsb.runCmd(NasdaqETLConfig.INCOME_STATEMENT, marketId, sdf.format(sc.getLatestOpenMarketDate(new Date())), sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_Fr_QuarterlyBalanceSheet(){
		nsb.runCmd(NasdaqETLConfig.BALANCE_SHEET, marketId, null, sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_Fr_QuarterlyCashFlow(){
		nsb.runCmd(NasdaqETLConfig.CASH_FLOW, marketId, null, sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_Fr_QuarterlyRevenueOneQ(){
		nsb.runCmd(NasdaqETLConfig.REVENUE, marketId, sdf.format(sc.getLatestOpenMarketDate(new Date())),  sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_Fr_QuarterlyRevenue(){
		nsb.runCmd(NasdaqETLConfig.REVENUE, marketId, null,  sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}	
	@Test
	public void testCmd_Issue_Dividend(){
		nsb.runCmd(NasdaqETLConfig.ISSUE_DIVIDEND_HISTORY, marketId, "2015-08-01", sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_Issue_ExDivSplit(){
		nsb.runCmd(NasdaqETLConfig.ISSUE_XDIVSPLIT_HISTORY, marketId, "2011-08-01", sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_Upcoming_Split(){
		nsb.runCmd(NasdaqETLConfig.ISSUE_UPCOMING_SPLIT, marketId, "2011-08-01", sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_Upcoming_Dividend(){
		nsb.runCmd(NasdaqETLConfig.ISSUE_UPCOMING_DIVIDEND, marketId, "2015-12-01", sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_QuoteShortInterest(){
		nsb.runCmd(NasdaqETLConfig.QUOTE_SHORT_INTEREST, marketId, "2015-08-01", sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_HolderInsiders(){
		nsb.runCmd(NasdaqETLConfig.HOLDING_INSIDERS, marketId, null, sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_HolderInsidersDateRange(){
		nsb.runCmd(NasdaqETLConfig.HOLDING_INSIDERS, marketId, "2015-02-01", sdf.format(sc.getLatestOpenMarketDate(new Date())));
	}
	@Test
	public void testCmd_HolderInstitutional(){
		nsb.runCmd(NasdaqETLConfig.HOLDING_INSTITUTIONAL, marketId, null, "2015-10-06");
	}
	
	@Test
	public void testReadyCrawl() throws Exception{
		String pFile = "client1-v2.properties";
		StockBase sb = new NasdaqStockBase(pFile, marketId, null, null);
		sb.fqReady(new Date());
	}
	
	@Test
	public void testCmd_Upcoming_Split_Hadoopless(){
		String[] csvs = nsb.runCmdHadoopless(NasdaqETLConfig.ISSUE_UPCOMING_SPLIT, marketId, "2011-08-01", 
				sdf.format(sc.getLatestOpenMarketDate(new Date())));
		logger.info(Arrays.toString(csvs));
		SqlUtil.insertCsvs(nsb.getCconf().getMetadbconf(), NasdaqSplitJDBCMapper.getInstance(), csvs);
	}
	@Test
	public void testCmd_Upcoming_Dividend_Hadoopless(){
		String[] csvs = nsb.runCmdHadoopless(NasdaqETLConfig.ISSUE_UPCOMING_DIVIDEND, marketId, "2015-12-24", 
				sdf.format(sc.getLatestOpenMarketDate(new Date())));
		logger.info(Arrays.toString(csvs));
		SqlUtil.insertCsvs(nsb.getCconf().getMetadbconf(), NasdaqUpcomingDivMapper.getInstance(), csvs);
	}
	
	//incremental crawl
	@Test
	public void testRunAllCmd() throws Exception{
		nsb.setStartDate(null);
		nsb.setEndDate(NasdaqETLConfig.date_Test_END_D1);
		nsb.runAllCmd(null);
		
		nsb.setStartDate(null);
		nsb.setEndDate(NasdaqETLConfig.date_Test_END_D3);
		nsb.runAllCmd(null);
	}
}
