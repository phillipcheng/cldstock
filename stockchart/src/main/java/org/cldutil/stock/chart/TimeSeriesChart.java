package org.cldutil.stock.chart;

import java.awt.Color;
import java.util.List;
import java.util.Date;

import org.cldutil.stock.common.CqIndicators;
import org.cldutil.stock.indicator.Indicator;
import org.cldutil.stock.indicator.RenderType;
import org.cldutil.stock.strategy.IntervalUnit;
import org.cldutil.stock.strategy.SelectStrategy;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

//with max 1 histogram and multiple timeseries
public class TimeSeriesChart extends ChartPanel implements DataChart{
	private static final long serialVersionUID = 1L;
	private String chartName;
	
	public TimeSeriesChart(JFreeChart jfc){
		super(jfc);
	}

	@Override
	public void setData(List<CqIndicators> cqilist, SelectStrategy bs, IntervalUnit unit, List<Date> dl){
		TimeSeriesCollection lineDataset = new TimeSeriesCollection();
		TimeSeriesCollection barDataset = new TimeSeriesCollection();
		
		for (String indName: bs.getIndMap().keySet()){
			Indicator ind = bs.getIndMap().get(indName);
			if (chartName.equals(ind.getChartId())){//this chart for this indicator
				List<TimeSeries> tsl = ChartUtil.getTSCollection(ind, cqilist, RenderType.line);
				for (TimeSeries ts:tsl){
					lineDataset.addSeries(ts);
				}
				List<TimeSeries> btsl = ChartUtil.getTSCollection(ind, cqilist, RenderType.bar);
				for (TimeSeries ts:btsl){
					barDataset.addSeries(ts);
				}
			}
		}
	
		XYPlot plot = (XYPlot) this.getChart().getPlot();
		ChartUtil.setTimeLine(unit, plot, cqilist.get(0).getCq().getStartTime());
		plot.setDataset(lineDataset);
		ChartUtil.setMarkers(plot, dl);
		
		//bar data will be mapped to 2nd axis
		int datasetIndex = 1;
		int axisIndex=1;
		final NumberAxis axis2 = new NumberAxis("Secondary");
        axis2.setAutoRangeIncludesZero(false);
        plot.setRangeAxis(axisIndex, axis2);
        plot.setDataset(datasetIndex, barDataset);
		plot.mapDatasetToRangeAxis(datasetIndex, axisIndex);
		XYItemRenderer renderer2 = new XYBarRenderer();
		plot.setRenderer(datasetIndex, renderer2);
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
	}
	
	public static TimeSeriesChart createTimeSeriesChart(){
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(null, null, null, null);
		XYPlot xyplot = (XYPlot)jfreechart.getPlot();
		xyplot.setDomainPannable(true);
		xyplot.setDomainZeroBaselineVisible(true);
		xyplot.setRangeZeroBaselineVisible(true);
		xyplot.setBackgroundPaint(Color.white);
		
		NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
		numberaxis.setAutoRangeIncludesZero(false);
		numberaxis.setUpperMargin(0.0D);
		numberaxis.setLowerMargin(0.0D);
		TimeSeriesChart chartpanel = new TimeSeriesChart(jfreechart);
		chartpanel.setDomainZoomable(true);
		chartpanel.setRangeZoomable(true);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}
	
	@Override
	public void setMyName(String name) {
		this.chartName = name;
	}
	@Override
	public String getMyName() {
		return chartName;
	}
	
}
