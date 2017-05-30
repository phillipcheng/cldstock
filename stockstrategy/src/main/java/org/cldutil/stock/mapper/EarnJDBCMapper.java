package org.cldutil.stock.mapper;

import org.cldutil.util.jdbc.JDBCMapper;

public abstract class EarnJDBCMapper extends JDBCMapper {
	
	public abstract boolean cumulativeEps();

}
