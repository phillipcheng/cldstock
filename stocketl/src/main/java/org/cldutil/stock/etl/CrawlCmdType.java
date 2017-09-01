package org.cldutil.stock.etl;

public enum CrawlCmdType {
	nondynamic,//meta data level: by stockid
	dynamic,//by stockid, datetime
	any
}
