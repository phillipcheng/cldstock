package org.cldutil.stock.chart;

import java.util.Date;
import java.util.List;

import org.cldutil.stock.common.CqIndicators;
import org.cldutil.stock.strategy.IntervalUnit;
import org.cldutil.stock.strategy.SelectStrategy;

public interface DataChart {
	public void setMyName(String name);
	public String getMyName();
	public void setData(List<CqIndicators> cqilist, SelectStrategy bs, IntervalUnit unit, List<Date> dl);
	
}
