package org.cldutil.stock.strategy.select;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.util.JsonUtil;
import org.cldutil.util.jdbc.DBConnConf;
import org.cldutil.stock.common.CqIndicators;
import org.cldutil.stock.common.DivSplit;
import org.cldutil.stock.strategy.OrderFilled;
import org.cldutil.stock.strategy.SelectCandidateResult;
import org.cldutil.stock.strategy.SelectStrategy;
import org.cldutil.stock.strategy.StockOrder.ActionType;
import org.cldutil.stock.strategy.persist.RangeEntry;
import org.cldutil.stock.strategy.persist.StrategyPersistMgr;

public class Range extends SelectStrategy {
	public static Logger logger = LogManager.getLogger(Range.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final String NAME="strategy.range.properties";
	
	public static final String SHIFT_RATE="shift.rate";
	public static final float default_shiftRate=0.02f;
	
	public Range(){
	}
	
	private float shiftRate = default_shiftRate;
	private float orgBuyPrice;
	private float currentPrice;
	private Date lastUpdateDt;
	private String symbol;
	private int buyLevel=0; //>=0, 0 is init level, the higher the cheaper

	@Override
	public void cleanup() {	
	}
	
	public String toString(){
		if (lastUpdateDt!=null){
			return String.format("%s:buy org price:%.3f, current price:%.3f, shiftRate:%.3f at %s", symbol, orgBuyPrice, 
				currentPrice, shiftRate, sdf.format(lastUpdateDt));
		}else{
			return String.format("%s:buy org price:%.3f, current price:%.3f, shiftRate:%.3f at %s", symbol, orgBuyPrice, 
					currentPrice, shiftRate, null);
		}
	}
	
	//called after initProp, before init, called once per set of parameters except for the symbol, template level
	@Override
	protected Map<String, SelectStrategy> genBsMap(PropertiesConfiguration pc, DBConnConf dbconf){
		Map<String, SelectStrategy> bsMap = new HashMap<String, SelectStrategy>();
		Date dt = new Date();
		List<RangeEntry> rel = StrategyPersistMgr.getRangeBuyPrice(dbconf, dt);
		for (RangeEntry re:rel){
			Range r = (Range) JsonUtil.deepClone(this);
			r.setOrgBuyPrice(re.getBuyPrice());
			r.setSymbol(re.getSymbol());
			r.setLastUpdateDt(re.getDt());
			bsMap.put(re.getSymbol(), r);
		}
		return bsMap;
	}
	
	@Override
	public void init(){//called once for per set of parameters per symbol
		super.init();
		if (super.getParams().containsKey(SHIFT_RATE)){
			shiftRate = Float.parseFloat((String) super.getParams().get(SHIFT_RATE));
		}
		logger.info(toString());
	}
	
	@Override
	public void xdivDay(DivSplit divsplit, DBConnConf dbconf){
		if (divsplit.getExDt().after(lastUpdateDt)){
			float dividend = divsplit.getDividend();
			if (dividend!=0){
				orgBuyPrice -= dividend;
				currentPrice -= dividend;
			}else{
				String splitInfo = divsplit.getInfo();
				String[] splits = splitInfo.split(":");
				if (splits.length>1){
					int a = Integer.parseInt(splits[0].trim());
					int b = Integer.parseInt(splits[1].trim());
					orgBuyPrice = orgBuyPrice*b/a;
					currentPrice = currentPrice*b/a;
				}
			}
			logger.info(String.format("org price to %.3f, currentPrice to %.3f, because of xdiv %s", orgBuyPrice, currentPrice, divsplit));
			StrategyPersistMgr.addRangeBuyPrice(dbconf, divsplit.getSymbol(), divsplit.getExDt(), orgBuyPrice);
		}
	}

	
	@Override
	public void tradeCompleted(OrderFilled or, boolean success){
		if (success){
			currentPrice = or.getAvgPrice() * (1-shiftRate);
			if (or.getSide()==ActionType.buy){
				buyLevel++;
			}else{
				buyLevel--;
			}
		}else{
			if (or.getSide()==ActionType.buy){//failed to buy recover the price
				currentPrice = or.getAvgPrice() / (1-shiftRate);
			}else{
				logger.info(String.format("failed to sell %s, maybe cancelled, keep the price.", or));
			}
		}
		if (currentPrice>orgBuyPrice){
			currentPrice = orgBuyPrice;
		}
		logger.info(String.format("tradeCompleted current price for %s is changed to %.3f", or.getSymbol(), currentPrice));
	}
	
	@Override
	public SelectCandidateResult selectByStream(CqIndicators cqi) {
		if (cqi.getCq().getLow()<currentPrice){
			currentPrice = currentPrice*(1-shiftRate);//to prevent tons of opp generated
			logger.info(String.format("range price for %s is changed to %.3f", cqi.getCq().getSymbol(), currentPrice));
			return new SelectCandidateResult(cqi.getCq().getSymbol(), cqi.getCq().getStartTime(), buyLevel, cqi.getCq().getLow());
		}else{
			return null;
		}
	}

	public float getOrgBuyPrice() {
		return orgBuyPrice;
	}
	public void setOrgBuyPrice(float orgBuyPrice) {
		this.orgBuyPrice = orgBuyPrice;
		this.currentPrice = orgBuyPrice;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Date getLastUpdateDt() {
		return lastUpdateDt;
	}

	public void setLastUpdateDt(Date lastUpdateDt) {
		this.lastUpdateDt = lastUpdateDt;
	}
}
