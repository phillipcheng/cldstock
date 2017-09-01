package org.cldutil.stock.persistence;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;

@Embeddable
public class MarketInfoId implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String marketId;
	private Date endDate;
	
	public MarketInfoId(String marketId, Date endDate){
		this.marketId=marketId;
		this.endDate=endDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}

}
