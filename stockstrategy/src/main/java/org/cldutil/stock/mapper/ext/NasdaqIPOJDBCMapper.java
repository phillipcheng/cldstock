package org.cldutil.stock.mapper.ext;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.util.MoneyUtil;
import org.cldutil.util.StringUtil;
import org.cldutil.util.jdbc.JDBCMapper;
import org.cldutil.util.jdbc.PersistObject;
import org.cldutil.stock.common.NasdaqIPO;
import org.cldutil.stock.config.NasdaqStockConfig;
import org.cldutil.stock.persistence.StockInfoId;

public class NasdaqIPOJDBCMapper extends JDBCMapper{
	Logger logger = LogManager.getLogger(NasdaqIPOJDBCMapper.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private NasdaqIPOJDBCMapper(){
		
	}
	private static NasdaqIPOJDBCMapper singleton = new NasdaqIPOJDBCMapper();
	
	public static NasdaqIPOJDBCMapper getInstance(){
		return singleton;
	}

	@Override
	public String getTableName() {
		return "NasdaqIPO";
	}
	
	@Override
	public boolean oneFetch() {
		return true;
	}

	@Override
	public Object getObject(ResultSet cursor) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	Company Name									Symbol	Market	Price	Shares Offered	Amount		Date Priced
	CHICKEN SOUP FOR THE SOUL ENTERTAINMENT, INC.	CSSE	NASDAQ Global	$12	2,500,000	$30,000,000	8/18/2017
after html to csv transform
		
																										2017-08-18
very rare: 
	NMI HOLDINGS; INC.,								NMIH,	OTCBB,					10.45-12.80,50096361,641233420.80,2013-11-08
	ZOE'S KITCHEN; INC.,							ZOES,	New York Stock Exchange,13.00-15.00,5833333,100624980,2014-04-11
	*/
	@Override
	public PersistObject getObject(String csv){
		NasdaqIPO nipo = new NasdaqIPO();
		String[] fields = csv.split(",");
		if (fields.length!=7){
			logger.error(String.format("unexpected csv for nasdaqIPO:%s", csv));
		}else{
			nipo.setCompanyName(fields[0]);
			StockInfoId siid = new StockInfoId(NasdaqStockConfig.MarketId_NASDAQ, fields[1]);//
			nipo.setId(siid);
			nipo.setSubmarket(fields[2]);
			try{
				double[] prices = MoneyUtil.getPriceRange(fields[3]);
				if (prices.length==1){
					nipo.setLprice(prices[0]);
					nipo.setUprice(prices[0]);
				}else{
					nipo.setLprice(prices[0]);
					nipo.setUprice(prices[1]);
				}
			}catch(Exception e){
				logger.error(String.format("illegal price in %s", csv));
			}
			nipo.setSharesOffered(StringUtil.getNumber(fields[4]));
			nipo.setAmount((long) MoneyUtil.getDollarValue(fields[5]));
			try{
				nipo.setIpoDate(sdf.parse(fields[6]));
			}catch(Exception e){
				logger.error("", e);
			}
		}
		return nipo;
	}
}
