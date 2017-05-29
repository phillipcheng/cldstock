package org.cld.stock.etl.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cld.datacrawl.test.TestBase;
import org.cld.stock.etl.base.SinaStockBase;
import org.cld.stock.etl.task.sina.SplitByStockMapper;
import org.cld.taskmgr.hadoop.HadoopTaskLauncher;
import org.junit.Before;
import org.junit.Test;

public class LauchSinaStock {
	private static Logger logger =  LogManager.getLogger(LauchSinaStock.class);
	
	private String propFile = "client1-v2-remote-dfs-local-yarn.properties";
	
	private SinaStockBase ssb;

	public LauchSinaStock(){
		super();
	}
	
	@Before
	public void setUp(){
		ssb = new SinaStockBase(propFile, null, null, null);
		ssb.getCconf().getHadoopCrawledItemFolder();
	}
	
	@Test
	public void testSplitByStock1(){
		Map<String, String> hadoopParams = new HashMap<String, String>();
		HadoopTaskLauncher.updateHadoopMemParams(1024, hadoopParams);
		HadoopTaskLauncher.hadoopExecuteTasks(ssb.getCconf(), hadoopParams, 
				new String[]{"/reminder/items/merge/sina-stock-market-fq"}, true, 
				"/reminder/items/mlinput/sina-stock-market-fq", 
				false, SplitByStockMapper.class, null, false);
	}
	
	@Test
	public void test_gen_nd_lable(){
		ssb.setSpecialParam("hdfs://192.85.247.104:19000/reminder/items/mlinput/sina-stock-market-fq,1,0:8,2");
		ssb.genNdLable();
	}
	
}