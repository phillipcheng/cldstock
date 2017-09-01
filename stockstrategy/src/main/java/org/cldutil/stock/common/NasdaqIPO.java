package org.cldutil.stock.common;

import javax.persistence.Entity;

import org.cldutil.stock.persistence.StockInfo;

@Entity
public class NasdaqIPO extends StockInfo{
	
	/*
	Company Name									Symbol	Market			Price	Shares Offered	Amount		Date Priced
	CHICKEN SOUP FOR THE SOUL ENTERTAINMENT, INC.	CSSE	NASDAQ Global	$12		2,500,000		$30,000,000	8/18/2017
	*/
	private double lprice;//lower price
	private double uprice;//upper price
	private long sharesOffered;
	private long amount;
	
	
	public long getSharesOffered() {
		return sharesOffered;
	}
	public void setSharesOffered(long sharesOffered) {
		this.sharesOffered = sharesOffered;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public double getLprice() {
		return lprice;
	}
	public void setLprice(double lprice) {
		this.lprice = lprice;
	}
	public double getUprice() {
		return uprice;
	}
	public void setUprice(double uprice) {
		this.uprice = uprice;
	}
	
}
