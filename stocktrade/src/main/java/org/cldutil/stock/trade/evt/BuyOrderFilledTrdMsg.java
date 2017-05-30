package org.cldutil.stock.trade.evt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.stock.strategy.SelectCandidateResult;
import org.cldutil.stock.strategy.SellStrategy;
import org.cldutil.stock.strategy.StockOrder;
import org.cldutil.stock.trade.AutoTrader;
import org.cldutil.stock.trade.StockOrderType;
import org.cldutil.stock.trade.TradeMsg;
import org.cldutil.stock.trade.TradeMsgPR;
import org.cldutil.stock.trade.persist.TradePersistMgr;
import org.cldutil.stock.trade.response.OrderResponse;

public class BuyOrderFilledTrdMsg extends TradeMsg {
	private static Logger logger =  LogManager.getLogger(BuyOrderFilledTrdMsg.class);
	
	String buyOrderId;

	public BuyOrderFilledTrdMsg(String buyOrderId, SelectCandidateResult scr, String bsName, Map<String, StockOrder> somap){
		super(TradeMsgType.buyOrderFilled, scr, bsName, somap);
		this.buyOrderId = buyOrderId;
	}
	
	public String toString(){
		return String.format("SM:%s", this.getMsgType());
	}
	
	private static final int STOP_THRESHOLD=10;
	/**
	 * [monitor buy order]
	 * 		|__ executed. 
	 * 			|__ when no stop sell: (for range strategy), 1 sell limit order submitted, 1 monitor sell limit order msg added
	 * 			|__ when has stop sell : 1 sell stop trailing order submitted (GTC), 2 msg added (monitor stop order, monitor price cross).	
	 *      |__ cancelled, remove me
	 */
	@Override
	public TradeMsgPR process(AutoTrader at) {
		List<TradeMsg> tml = new ArrayList<TradeMsg>();
		TradeMsgPR tmpr = new TradeMsgPR();
		SellStrategy ss = at.getSs(scr.getSymbol(), bsName);
		StockOrder selllimit = somap.get(StockOrderType.selllimit.name());
		if (ss.getStopPercentage()>STOP_THRESHOLD){
			OrderResponse or = at.getTm().trySubmit(selllimit, at.isPreview()); //submit limit sell order
			if (OrderResponse.SUCCESS.equals(or.getError())){
				logger.info(String.format("limit sell order submitted. %s", selllimit));
				TradePersistMgr.updateLimitSellOrderId(at.getDbConf(), buyOrderId, or.getClientorderid());
				MonitorSellLimitOrderTrdMsg mslo = new MonitorSellLimitOrderTrdMsg(or.getClientorderid(), scr, bsName, somap);
				selllimit.setOrderId(or.getClientorderid());//set this client id into the stock order context
				tml.add(mslo);//
				tmpr.setExecuted(true);
				tmpr.setNewMsgs(tml);
				return tmpr;
			}else{
				//TODO error handling
				logger.error(String.format("sell limit order error: %s, response: %s", selllimit, or));
			}
		}else{
			//submit 1 sell stop trailing order, 1 monitor order msg and 1 monitor price msg
			StockOrder sellstop = somap.get(StockOrderType.sellstop.name());
			OrderResponse or = at.getTm().trySubmit(sellstop, at.isPreview()); //submit stop order
			if (OrderResponse.SUCCESS.equals(or.getError())){
				logger.info(String.format("sellstop order submitted. %s", sellstop));
				TradePersistMgr.updateStopSellOrderId(at.getDbConf(), buyOrderId, or.getClientorderid());
				MonitorSellStopOrderTrdMsg msso = new MonitorSellStopOrderTrdMsg(or.getClientorderid(), scr, bsName, somap);
				sellstop.setOrderId(or.getClientorderid());//set this client id into the stock order context
				tml.add(msso);//
				MonitorSellPriceTrdMsg msp = new MonitorSellPriceTrdMsg(sellstop.getSymbol(), selllimit.getLimitPrice(), scr, bsName, somap);
				tml.add(msp);//
				tmpr.setExecuted(true);
				tmpr.setNewMsgs(tml);
				return tmpr;
			}else{
				//TODO error handling
				logger.error(String.format("sell stop order error: %s, response: %s", sellstop, or));
			}
		}
		return tmpr;
	}
}
