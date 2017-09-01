package org.cldutil.stock.persistence;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class StockInfoId implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String marketId;//within this market, stockid is unique
	private String stockId;
	
	public StockInfoId(String marketId, String stockId){
		this.marketId=marketId;
		this.setStockId(stockId);
	}
	
	
	public String getMarketId() {
		return marketId;
	}
	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}


	public String getStockId() {
		return stockId;
	}


	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

}
