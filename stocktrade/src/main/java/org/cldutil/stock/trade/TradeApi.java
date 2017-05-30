package org.cldutil.stock.trade;

import java.util.List;
import java.util.Map;

import org.cldutil.stock.strategy.StockOrder;
import org.cldutil.stock.strategy.StockOrder.ActionType;
import org.cldutil.stock.trade.response.Balance;
import org.cldutil.stock.trade.response.Holding;
import org.cldutil.stock.trade.response.OrderResponse;
import org.cldutil.stock.trade.response.OrderStatus;
import org.cldutil.stock.trade.response.Quote;

public interface TradeApi {
	public OrderResponse previewOrder(StockOrder so);
	public OrderResponse makeOrder(StockOrder so);
	public OrderResponse cancelOrder(String clientOrderId, ActionType at, String symbol, int quantity);
	public Balance getBalance();
	public List<Holding> getHolding();
	public Map<String, OrderStatus> getOrderStatus();
	public OrderStatus getTheOrderStatus(String orderId);
	public List<Quote> getQuotes(String[] stockids, String[] fids, boolean extendedHour);
	public OrderResponse trySubmit(StockOrder sobuy, boolean submit);
}
