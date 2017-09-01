package org.cldutil.stock.persistence;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.util.jdbc.PersistObject;

/*
 Status:
   RUNNING(1),
   SUCCEEDED(2),
   FAILED(3),
   PREP(4),
   KILLED(5);
*/
@Entity
public class CmdStatus implements PersistObject {
	protected static Logger logger =  LogManager.getLogger(CmdStatus.class);
	
	@Id
	private CmdStatusId id;

	@ElementCollection(targetClass = Integer.class)
	@CollectionTable(name = "JobStatusMap")
    @MapKeyColumn(name="jobId")
    @Column(name="status")
	private Map<String, Integer> jsMap = new HashMap<String,Integer>();
	
	public CmdStatus(){
	}
	
	@Override
	public void copy(PersistObject ci){
		if (ci instanceof CmdStatus){
			this.jsMap=((CmdStatus)ci).jsMap;
		}else{
			logger.error("can't copy " + ci);
		}
	}
	
	public CmdStatus(String marketId, String cmdName, Date endTime){
		this.id = new CmdStatusId(marketId, cmdName, endTime);
	}

	public Map<String, Integer> getJsMap() {
		return jsMap;
	}

	public void setJsMap(Map<String, Integer> jsMap) {
		this.jsMap = jsMap;
	}
	
	@Override
	public CmdStatusId getId() {
		return id;
	}

	public void setId(CmdStatusId id) {
		this.id = id;
	}
}
