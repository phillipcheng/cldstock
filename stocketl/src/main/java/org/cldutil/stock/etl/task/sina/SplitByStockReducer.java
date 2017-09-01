package org.cldutil.stock.etl.task.sina;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.datacrawl.CrawlConf;
import org.cldutil.datacrawl.CrawlUtil;
import org.cldutil.datacrawl.client.CrawlClientUtil;
import org.cldutil.etl.fci.AbstractCrawlItemToCSV;
import org.cldutil.taskmgr.hadoop.HadoopTaskLauncher;
import org.cldutil.util.entity.CrawledItem;
import org.cldutil.util.entity.CrawledItemId;
import org.cldutil.stock.config.SinaStockConfig;
import org.cldutil.stock.etl.ETLUtil;


public class SplitByStockReducer extends Reducer<Text, Text, Text, Text>{
	private static Logger logger =  LogManager.getLogger(SplitByStockReducer.class);
	public static String sep = "_";
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private MultipleOutputs<Text, Text> mos;
	
	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		mos = new MultipleOutputs<Text,Text>(context);
	}
	
	@Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        mos.close();
    }
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		//value: stockid, date, xxx
		for (Text v:values){
			String[] vs = v.toString().split(",");
			if (vs.length>1){
				StringBuffer sb = new StringBuffer();
				String stockid = vs[0];
				for (int i=1; i<vs.length; i++){
					if (i>1){
						sb.append(",");
					}
					sb.append(vs[i]);
				}
				mos.write(HadoopTaskLauncher.NAMED_OUTPUT_TXT, key, sb.toString(), stockid);
			}
		}
	}
	
}
