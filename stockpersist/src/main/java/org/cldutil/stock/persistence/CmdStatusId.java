package org.cldutil.stock.persistence;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Embeddable
public class CmdStatusId implements Serializable{
	private static final long serialVersionUID = 1L;
	protected static Logger logger =  LogManager.getLogger(CmdStatusId.class);
	
	private String marketId;
	private String cmdName;
	private Date endTime;//(lastEndTime, endTime] is the range of data the cmd is working on
	
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}
	public String getCmdName() {
		return cmdName;
	}
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public CmdStatusId(String marketId, String cmdName, Date endTime){
		this.marketId = marketId;
		this.cmdName = cmdName;
		this.endTime = endTime;
	}
	
	public String toString(){
		return String.format("%s_%s_%s", marketId, cmdName, endTime);
	}
}
