package com.db.trade.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.db.trade.entity.EntityTrade;
import com.db.trade.model.Trade;
import com.db.trade.util.DateUtil;
import com.mongodb.client.result.UpdateResult;

@Service
public class TradeService {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	SeqGeneratorService seqGeneratorService;

	public List<Trade> getAllTrades() {
		Query query = new Query()
				.with(Sort.by(Sort.Direction.ASC, "tradeId").and(Sort.by(Sort.Direction.DESC, "version")));
		List<EntityTrade> trades = mongoTemplate.find(query, EntityTrade.class);
		return populateModel(trades);
	}

	private List<Trade> populateModel(List<EntityTrade> trades) {

		List<Trade> modelTrades = new ArrayList<Trade>(trades.size());
		for (EntityTrade trade : trades) {
			Trade modelTrade = new Trade();
			modelTrade.setId(trade.getId());
			modelTrade.setBookId(trade.getBookId());
			modelTrade.setCounterPartyId(trade.getCounterPartyId());
			modelTrade.setCreatedDate(DateUtil.dateToStringConversion(trade.getCreatedDate()));
			modelTrade.setExpired(trade.getExpired());
			modelTrade.setMaturityDate(DateUtil.dateToStringConversion(trade.getMaturityDate()));
			modelTrade.setTradeId(trade.getTradeId());
			modelTrade.setVersion(trade.getVersion());
			modelTrades.add(modelTrade);
		}
		return modelTrades;
	}

	public String processTrades(Trade trade) throws Exception {

		if (DateUtil.isvalidDate(trade.getMaturityDate())) {
			Query query = new Query().with(Sort.by(Sort.Direction.DESC, "version")).limit(1);
			query.addCriteria(Criteria.where("tradeId").is(trade.getTradeId()));
			EntityTrade entityTrade = mongoTemplate.findOne(query, EntityTrade.class);

			if (null != entityTrade && entityTrade.getVersion() > trade.getVersion()) {
				throw new Exception("Trade with lower version number is not allowed"); 
			} else if (null != entityTrade && entityTrade.getVersion() == trade.getVersion()) {
				entityTrade.setCounterPartyId(trade.getCounterPartyId());
				entityTrade.setBookId(trade.getBookId());
				entityTrade.setMaturityDate(DateUtil.stringToDateConverter(trade.getMaturityDate()));
				EntityTrade entity = (EntityTrade) mongoTemplate.save(entityTrade);
				return "Trade Updated Successfully";
			} else {

				entityTrade = new EntityTrade();
				entityTrade.setId(seqGeneratorService.genSeq("db_seq"));
				entityTrade.setBookId(trade.getBookId());

				if (null == entityTrade.getCreatedDate()) {
					entityTrade.setCreatedDate(new Date());
				}
				entityTrade.setCounterPartyId(trade.getCounterPartyId());
				entityTrade.setExpired("N");
				entityTrade.setMaturityDate(DateUtil.stringToDateConverter(trade.getMaturityDate()));
				entityTrade.setTradeId(trade.getTradeId());
				entityTrade.setVersion(trade.getVersion());
				EntityTrade entity = (EntityTrade) mongoTemplate.save(entityTrade);
				return "Trade processed Successfully";
			}
			
		} else {
			throw new Exception("Trade with prior maturity date is not allowed"); 
		}
	}

	public void updateTradeExpiry() {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("maturityDate").lt(new Date()).and("expired").ne("Y"));
		
		Update update = new Update();
		update.set("expired", "Y");

		UpdateResult result = mongoTemplate.updateMulti(query, update, EntityTrade.class);
	}

}
