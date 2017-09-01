package org.cldutil.stock.etl.split;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.datacrawl.CrawlConf;
import org.cldutil.stock.etl.base.ETLConfig;
import org.cldutil.taskmgr.TaskUtil;
import org.cldutil.taskmgr.entity.Task;
import org.cldutil.taskmgr.hadoop.HadoopTaskLauncher;
import org.cldutil.util.StringUtil;

public abstract class SplitTask {
	private static Logger logger =  LogManager.getLogger(SplitTask.class);
	
	protected ETLConfig ec;
	protected CrawlConf cconf;
	protected String propfile;
	protected Task t;
	protected String cmd;
	protected Map<String, Object> params;
	
	public SplitTask(ETLConfig ec, CrawlConf cconf, String propfile, Task t,  String cmd, Map<String, Object> params){
		this.ec = ec;
		this.cconf = cconf;
		this.propfile = propfile;
		this.t = t;
		this.cmd = cmd;
		this.params = params;
	}
	
	public abstract List<Task> getTasks();
	
	public abstract String getTaskName();
	
	//input file for mapreduce job
	public String genInputFile() {
		try {
			Configuration conf = HadoopTaskLauncher.getHadoopConf(cconf);
			FileSystem fs = FileSystem.get(conf);
			StringBuffer fileContent = new StringBuffer();
			List<Task> tasks = getTasks();
			for (Task t: tasks){
				String fn = TaskUtil.taskToJson(t);
				fileContent.append(fn).append("\n");
			}
			String escapedName = StringUtil.escapeFileName(getTaskName());
			String taskFileName = cconf.getHdfsTaskFolder() + "/" + escapedName;
			logger.info(String.format("task file: %s with length %d generated.", taskFileName, fileContent.length()));
			Path fileNamePath = new Path(taskFileName);
			OutputStream fin = fs.create(fileNamePath);
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fin, "UTF-8" ));
			br.write(fileContent.toString());
			br.close();
			return taskFileName;
		}catch(Exception e){
			logger.error("", e);
			return null;
		}
	}

}
