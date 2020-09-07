package com.db.trade.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.db.trade.entity.DbSequence;

@Service
public class SeqGeneratorService {

	@Autowired
	private MongoOperations mongoOperations;

	public long genSeq(String sequenceName) {
		DbSequence count = mongoOperations.findAndModify(query(where("_id").is(sequenceName)),
				new Update().inc("seq", 1), options().returnNew(true).upsert(true), DbSequence.class);
		return !Objects.isNull(count) ? count.getSeq() : 0;
	}

}
