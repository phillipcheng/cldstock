package org.cldutil.stock.strategy.select;

import java.util.Random;

import org.cldutil.stock.common.CqIndicators;
import org.cldutil.stock.strategy.SelectCandidateResult;
import org.cldutil.stock.strategy.SelectStrategy;

public class RandomD extends SelectStrategy {
	
	public RandomD(){
	}

	//only used in history to compare, will not be used in real stream mode
	@Override
	public SelectCandidateResult selectByStream(CqIndicators cqi) {
		SelectCandidateResult scr = null;
		Random r = new Random();
		//emit a random value for stock on trading day dt
		if (checkValid(cqi.getCq())){
			scr = new SelectCandidateResult(cqi.getCq().getSymbol(), sc.getNormalTradeStartTime(cqi.getCq().getStartTime()), 
					r.nextFloat(), cqi.getCq().getClose());
		}
		return scr;
	}

	@Override
	public void cleanup() {
		
	}
}
