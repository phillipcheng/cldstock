package org.cldutil.stock.trade.evt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.stock.strategy.OrderFilled;
import org.cldutil.stock.strategy.SelectCandidateResult;
import org.cldutil.stock.strategy.StockOrder;
import org.cldutil.stock.strategy.TradeStrategy;
import org.cldutil.stock.strategy.StockOrder.ActionType;
import org.cldutil.stock.strategy.StockOrder.OrderType;
import org.cldutil.stock.strategy.select.Range;
import org.cldutil.stock.trade.AutoTrader;
import org.cldutil.stock.trade.StockOrderType;
import org.cldutil.stock.trade.TradeMsg;
import org.cldutil.stock.trade.TradeMsgPR;
import org.cldutil.stock.trade.persist.StockPosition;
import org.cldutil.stock.trade.persist.TradePersistMgr;
import org.cldutil.stock.trade.response.Balance;
import org.cldutil.stock.trade.response.OrderResponse;

/*
 * buy opportunity found trd-msg
 */
public class BuyOppTrdMsg extends TradeMsg {
	private static Logger logger =  LogManager.getLogger(BuyOppTrdMsg.class);
	
	TradeStrategy ts;
	
	public BuyOppTrdMsg(TradeStrategy ts, SelectCandidateResult scr){
		super(TradeMsgType.buyOppFound, scr, ts.getBs().getName(), null);
		this.ts = ts;
	}

	public String toString(){
		return String.format("scr:%s, ts:%s", scr, ts);
	}
	
	@Override
	public TradeMsgPR process(AutoTrader at) {
		logger.info(String.format("ts:%s", ts));
		
		TradeMsgPR tmpr = new TradeMsgPR();
		List<TradeMsg> tml = new ArrayList<TradeMsg>();
		if (scr!=null){
			float moneyAmount = at.getUseAmount();
			if (ts.getBs().getName().equals(Range.NAME)){
				float level = scr.getValue(); //for range strategy, the value field is buyLevel
				moneyAmount = moneyAmount * (1+level*at.getBuyLvlFactor());
			}
			logger.debug(String.format("bs name:%s, moneyAmount:%.2f", ts.getBs().getName(), moneyAmount));
			Map<String, StockOrder> somap = AutoTrader.genStockOrderMap(scr, ts.getSs(), moneyAmount);
			StockOrder buyOrder = somap.get(StockOrderType.buy.name());
			if (buyOrder!=null){
				//of for callback
				OrderFilled of = new OrderFilled(scr.getSymbol(), buyOrder.getQuantity(), scr.getBuyPrice(), ActionType.buy, OrderType.limit);
				Balance balance = at.getTm().getBalance();
				if (balance.canBuy(moneyAmount)){
					OrderResponse or = at.getTm().trySubmit(buyOrder, at.isPreview());
					if (OrderResponse.SUCCESS.equals(or.getError()) && !at.isPreview()){
						StockPosition sp = new StockPosition(scr, bsName, somap, or.getClientorderid());
						TradePersistMgr.createStockPosition(at.getDbConf(), sp);
						TradeMsg mbo = new MonitorBuyOrderTrdMsg(or.getClientorderid(), scr, bsName, somap);
						tml.add(mbo);//buy order submitted, monitor buy order msg generated
					}else{
						ts.getBs().tradeCompleted(of, false);
						logger.error(String.format("buy error: buy order: %s, response: %s", buyOrder, or));
					}
				}else{
					ts.getBs().tradeCompleted(of, false);
					logger.info(String.format("balance %s less then needed %.2f", balance, moneyAmount));
				}
			}else{
				logger.error(String.format("SYSTEM error! buy order not found in somap:%s", somap));
			}
		}else{
			logger.error(String.format("SYSTEM error! scr is null"));
		}
		
		tmpr.setExecuted(true);
		tmpr.setNewMsgs(tml);
		return tmpr;
	}
}
