package org.cldutil.stock.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.datastore.hibernate.HibernatePersistMgr;
import org.cldutil.util.jdbc.DBConnConf;
import org.cldutil.util.jdbc.JDBCMapper;
import org.cldutil.util.jdbc.PersistObject;
import org.cldutil.util.jdbc.SqlUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * @author Cheng Yi
 */
public class StockPersistMgr extends HibernatePersistMgr{
	private static Logger logger =  LogManager.getLogger(StockPersistMgr.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private MarketInfo getLatestMarketInfo(String marketId, Session ses){
		try {
			List<MarketInfo> results = ses.createCriteria(MarketInfo.class)
				.add(Restrictions.eq("id.marketId", marketId))
				.addOrder(Property.forName("id.endDate").desc())
				.setMaxResults(1)
				.list();
			MarketInfo p = null;
			if (results != null && !results.isEmpty()) {
				p = (MarketInfo) results.get(0);
			}
			return p;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}
	
	public MarketInfo getLatestMarketInfo(String marketId){
		Session ses = hibernateSF.openSession();
		try {
			return getLatestMarketInfo(marketId, ses);
		}finally{
			if (ses!=null){
				ses.close();
			}
		}
	}
	
	public Map<String, Date> getStockIPOData(String marketId){
		Map<String, Date> ipoDateMap = new HashMap<String, Date>();
		Session ses = hibernateSF.openSession();
		try {
			List<StockInfo> results = ses.createCriteria(StockInfo.class)
				.add(Restrictions.eq("id.marketId", marketId))
				.list();
			for (StockInfo si:results){
				ipoDateMap.put(si.getId().getStockId(), si.getIpoDate());
			}
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}finally{
			ses.close();
		}
		
		return ipoDateMap;
	}
	
	///////////////////
	
	public static void truncateTable(DBConnConf dbconf, String tableName){
		Connection con = null;
		try{
			con = SqlUtil.getConnection(dbconf);
			String sql = String.format("truncate table %s", tableName);
	        logger.info(String.format("start execute update query:%s", sql));
	        SqlUtil.execUpdateSQL(con, sql);
	        logger.info(String.format("finish execute update query:%s", sql));
		}catch (Exception e){
			logger.error("", e);
		}finally{
			SqlUtil.closeResources(con, null);
		}
	}
	
	public static void loadData(DBConnConf dbconf, String fileName, String tableName){
		Connection con = null;
		Statement stmt = null;
		try{
			con = SqlUtil.getConnection(dbconf);
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	        String query = String.format(
	        		"LOAD DATA LOCAL INFILE '%s' INTO TABLE %s CHARACTER SET UTF8 COLUMNS TERMINATED BY ',' LINES TERMINATED BY '\n'", 
	        		fileName, tableName);
	        query = query.replace("\\", "\\\\");
	        logger.info(String.format("start execute update query:%s", query));
	        stmt.executeUpdate(query);
	        logger.info(String.format("finish execute update query:%s", query));
		}catch (Exception e){
			logger.error("", e);
		}finally{
			SqlUtil.closeResources(con, stmt);
		}
	}
	
	public static List<Object> getDataByDate(DBConnConf dbconf, JDBCMapper tableMapper, Date ed, String dateFieldName){
		Connection con = null;
		String sql = null;
		if (dateFieldName==null){
			sql = String.format("select * from %s where dt='%s'", tableMapper.getTableName(), sdf.format(ed));
		}else{
			sql = String.format("select * from %s where %s='%s'", tableMapper.getTableName(), dateFieldName, sdf.format(ed));
		}
		try{
			con = SqlUtil.getConnection(dbconf);
			List<Object> lo = (List<Object>) SqlUtil.getObjectsByParam(sql, new Object[]{}, 
					con, -1, -1, "", 
					tableMapper);
			return lo;
		}catch(Exception e){
			logger.error("", e);
			return null;
		}finally{
			SqlUtil.closeResources(con, null);
		}
	}
	
	public static List<Object> getDataByDate(DBConnConf dbconf, JDBCMapper tableMapper, Date ed){
		return getDataByDate(dbconf, tableMapper, ed, null);
	}
	
	//in descending order
	public static List<Object> getDataByStockDateLimit(DBConnConf dbconf, JDBCMapper tableMapper, String stockId, Date ed, int limit){
		Connection con = null;
		String sql = String.format("select * from %s where stockid='%s' and dt<='%s' order by dt desc limit %d", 
				tableMapper.getTableName(), stockId, sdf.format(ed), limit);
		try{
			con = SqlUtil.getConnection(dbconf);
			List<Object> lo = (List<Object>) SqlUtil.getObjectsByParam(sql, new Object[]{}, 
					con, -1, -1, "", 
					tableMapper);
			return lo;
		}catch(Exception e){
			logger.error("", e);
			return null;
		}finally{
			SqlUtil.closeResources(con, null);
		}
	}
	
	//get the get last update date query
	public static String getMarketLUDateQueryByTables(Set<String> tables){
		String sql = null;
		if (tables == null){
			return null;
		}
		if (tables.size()==1){
			sql = String.format("select max(dt) from %s", tables.iterator().next());
		}else{
			StringBuffer sb = new StringBuffer("select max(ludt) from (");
			int i=0;
			Iterator<String> it = tables.iterator();
			while (it.hasNext()){
				String table = it.next();
				if (i>0){
					sb.append(" union ");
				}
				sb.append(String.format("select max(dt) as ludt from %s", table));
				i++;
			}
			sb.append(") as marketlu");
			sql = sb.toString();
		}
		return sql;
	}
	
	public static Map<String, Date> getStockLUDateByCmd(String query, DBConnConf dbconf){
		Map<String, Date> stockLUMap = new HashMap<String, Date>();
		Connection con = null;
		Statement stmt = null;
		try{
			con = SqlUtil.getConnection(dbconf);
			stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(query);
			while (res.next()){
				ResultSetMetaData rsmd = res.getMetaData();
				try{
					if (rsmd.getColumnType(2)==Types.TIMESTAMP){
						stockLUMap.put(res.getString(1), res.getTimestamp(2));
					}else if (rsmd.getColumnType(2)==Types.DATE){
						stockLUMap.put(res.getString(1), res.getDate(2));
					}
				}catch(Exception e){
					try{
						logger.error(String.format("error converting for stockid:%s, max dt: %s", res.getString(1), res.getString(2)), e);
					}catch(Exception e1){
						logger.error("", e1);
					}
				}
			}
			res.close();
		}catch(Exception e){
			logger.error(String.format("exceptin while execute %s", query), e);
		}finally{
			SqlUtil.closeResources(con, stmt);
		}
		return stockLUMap;
	}
	
	public static Date getMarketLUDateByCmd(Set<String> tables, DBConnConf dbconf){
		Connection con = null;
		Statement stmt = null;
		String query = "";
		Date d = null;
		try{
			con = SqlUtil.getConnection(dbconf);		
			query = getMarketLUDateQueryByTables(tables);
			stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(query);
			if (res.next()){
				d = res.getDate(1);
			}
			res.close();
		}catch(Exception e){
			logger.error(String.format("exceptin while execute %s", query), e);
		}finally{
			SqlUtil.closeResources(con, stmt);
		}
		return d;
	}
	
}
