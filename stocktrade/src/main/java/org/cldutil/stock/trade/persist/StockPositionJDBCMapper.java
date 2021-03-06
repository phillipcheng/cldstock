package org.cldutil.stock.trade.persist;

import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.util.jdbc.JDBCMapper;

/**
 *
 */
public class StockPositionJDBCMapper extends JDBCMapper{
	Logger logger = LogManager.getLogger(StockPositionJDBCMapper.class);
	
	//symbol varchar(50), orderqty decimal(10,2), orderprice decimal(10,2), buySubmitDt DATETIME, buyOrderId varchar(50), 
	//stopSellOrderId varchar(50), limitSellOrderId varchar(50), bsName varchar(100), soMap varchar(2000)
	private StockPositionJDBCMapper(){
		
	}
	private static StockPositionJDBCMapper singleton = new StockPositionJDBCMapper();
	
	public static StockPositionJDBCMapper getInstance(){
		return singleton;
	}
/*
*/
	@Override
	public StockPosition getObject(ResultSet cursor) {
    	try{
    		StockPosition b= new StockPosition(
	    			cursor.getString(1), cursor.getInt(2), cursor.getFloat(3), cursor.getTimestamp(4), cursor.getString(5), 
	    			cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
	    	return b;
    	}catch(Exception e){
    		logger.error("", e);
    		return null;
    	}
	}
	
	@Override
	public String getTableName() {
		return "StockPosition";
	}

}
