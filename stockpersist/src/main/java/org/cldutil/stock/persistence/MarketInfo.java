package org.cldutil.stock.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.datastore.hibernate.StringListConverter;
import org.cldutil.util.jdbc.PersistObject;

/**
 * MarketInfo define the stockIds for a specific marketId(Nasdaq,etc) at a given day (end date)
 * @author chengyi
 *
 */
@Entity
@Table(name = "MarketInfo")
public class MarketInfo implements PersistObject {
	protected static Logger logger =  LogManager.getLogger(MarketInfo.class);
	@Id
	private MarketInfoId id;
	
	@Lob 
	@Column(length=9999999)
	@Convert(converter = StringListConverter.class)
	private List<String> stockIds;
	
	public MarketInfo(){
	}
	
	public List<String> getStockIds() {
		return stockIds;
	}
	public void setStockIds(List<String> stockIds) {
		this.stockIds = stockIds;
	}
	
	@Override
	public MarketInfoId getId() {
		return id;
	}
	public void setId(MarketInfoId id) {
		this.id = id;
	}
	
	public MarketInfo(String marketId, Date endDate, List<String> stockIds){
		id = new MarketInfoId(marketId, endDate);
		this.stockIds = stockIds;
	}
	@Override
	public void copy(PersistObject mi){
		if (mi!=null){
			if (mi instanceof MarketInfo){
				stockIds = ((MarketInfo)mi).getStockIds();
			}else{
				logger.error("can't copy " + mi);
			}
		}
	}
}
