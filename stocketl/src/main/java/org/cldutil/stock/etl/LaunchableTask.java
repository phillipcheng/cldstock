package org.cldutil.stock.etl;

import org.cldutil.datacrawl.CrawlConf;

public interface LaunchableTask {
	//return jobId list
	public String[] launch(String propfile, String baseMarketId, CrawlConf cconf, String datePart, String[] cmds);

}
