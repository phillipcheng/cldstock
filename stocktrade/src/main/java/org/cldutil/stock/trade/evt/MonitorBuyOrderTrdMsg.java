package org.cldutil.stock.trade.evt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cldutil.stock.strategy.OrderFilled;
import org.cldutil.stock.strategy.SelectCandidateResult;
import org.cldutil.stock.strategy.SelectStrategy;
import org.cldutil.stock.strategy.StockOrder;
import org.cldutil.stock.trade.AutoTrader;
import org.cldutil.stock.trade.TradeKingConnector;
import org.cldutil.stock.trade.TradeMsg;
import org.cldutil.stock.trade.TradeMsgPR;
import org.cldutil.stock.trade.response.OrderStatus;

public class MonitorBuyOrderTrdMsg extends TradeMsg {
	private static Logger logger =  LogManager.getLogger(MonitorBuyOrderTrdMsg.class);
	private String orderId;
	
	public MonitorBuyOrderTrdMsg(String buyOrderId, SelectCandidateResult scr, String bsName, Map<String, StockOrder> somap){
		super(TradeMsgType.monitorBuyLimitOrder, scr, bsName, somap);
		this.orderId = buyOrderId;
	}
	
	public String toString(){
		return String.format("SM:%s,%s", this.getMsgType(), orderId);
	}


	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
	/**
	 * [monitor buy order]
	 * 		|__ executed. 1 buy order filled msg generated
	 *      |__ cancelled, remove me
	 */
	@Override
	public TradeMsgPR process(AutoTrader at) {
		OrderStatus os = at.getTm().getTheOrderStatus(getOrderId());
		TradeMsgPR tmpr = new TradeMsgPR();
		List<TradeMsg> tml = new ArrayList<TradeMsg>();
		if (os!=null){
			OrderFilled of = TradeKingConnector.toOrderFilled(os);
			SelectStrategy bs = at.getBs(scr.getSymbol(), bsName);
			if (bs!=null){
				if (OrderStatus.FILLED.equals(os.getStat())){
					//callback
					bs.tradeCompleted(of, true);
					BuyOrderFilledTrdMsg bof = new BuyOrderFilledTrdMsg(orderId, this.scr, bsName, this.getSomap());
					tml.add(bof);
					tmpr.setNewMsgs(tml);
					tmpr.setExecuted(true);
					return tmpr;
				}else if (OrderStatus.CANCELED.equals(os.getStat())){
					logger.info(String.format("order %s cancelled", os));
					bs.tradeCompleted(of, false);
					tmpr.setExecuted(true);
					return tmpr;
				}else if (OrderStatus.PARTIALLY_FILLED.equals(os.getStat())){//since this msg might be played for a long time
					logger.debug(String.format("order %s is partially filled.", os.getOrderId()));
				}else{
					logger.info(String.format("status is %s for buy order %s", os.getStat(), os.getOrderId()));
				}
			}else{
				logger.error(String.format("SYSTEM error, bs not found for name %s", bsName));
			}
		}else{
			logger.info(String.format("%s not found in the recent orders.", getOrderId()));
		}
		return tmpr;
	}
}
