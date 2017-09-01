package org.cldutil.stock.persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.util.jdbc.PersistObject;

@Entity
@Table(name = "StockInfo")
public class StockInfo implements PersistObject {
	protected static Logger logger =  LogManager.getLogger(StockInfo.class);
	@Id
	private StockInfoId id;
	private Date ipoDate;
	private String companyName;
	private String submarket;
	
	public StockInfoId getId() {
		return id;
	}
	public void setId(StockInfoId id) {
		this.id = id;
	}
	public Date getIpoDate() {
		return ipoDate;
	}
	public void setIpoDate(Date ipoDate) {
		this.ipoDate = ipoDate;
	}
	
	public void copy(PersistObject si){
		if (si instanceof PersistObject){
			this.ipoDate = ((StockInfo)si).getIpoDate();
		}else{
			logger.error("can't copy " + si);
		}
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSubmarket() {
		return submarket;
	}
	public void setSubmarket(String submarket) {
		this.submarket = submarket;
	}
}
