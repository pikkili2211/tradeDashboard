package com.db.trade;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.assertj.core.api.Assertions;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.db.trade.controller.TradeController;
import com.db.trade.model.Trade;
import com.db.trade.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTradeController {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private TradeController controller;
	
	@Test	
	public void test_1_ContexLoads() throws Exception {
		Assertions.assertThat(controller).isNotNull();
	}

	@Test
	public void test_2_ReturnAllTrades() throws Exception {
		this.mockMvc.perform(get("/trades/alltrades")).andDo(print()).andExpect(status().isOk()).andExpect(content()
				.string(""));

	}

	@Test
	public void test_3_ProcessTradesInsert() throws Exception {
		
		Trade t1 = new Trade();
		t1.setTradeId("T1");
		t1.setVersion(1);
		t1.setBookId("B1");
		t1.setMaturityDate("2020-09-20");
		t1.setCreatedDate(DateUtil.dateToStringConversion(new Date()));
		t1.setExpired("N");
		t1.setCounterPartyId("CP-1");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/trades/processtrades").content(mapper.writeValueAsString(t1))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string("Trade processed Successfully"));
		
		//Trade Update
		
		Trade t2 = new Trade();
		t2.setTradeId("T1");
		t2.setVersion(1);
		t2.setBookId("B1");
		t2.setMaturityDate("2020-9-28");
		t2.setCreatedDate(DateUtil.dateToStringConversion(new Date()));
		t2.setExpired("N");
		t2.setCounterPartyId("CP-1");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/trades/processtrades").content(mapper.writeValueAsString(t2))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string("Trade Updated Successfully"));


	}
	
	@Test
	public void test_4_ProcessTradesUpdate() throws Exception {
		Trade t2 = new Trade();
		t2.setTradeId("T1");
		t2.setVersion(2);
		t2.setBookId("B1");
		t2.setMaturityDate("2020-9-28");
		t2.setCreatedDate(DateUtil.dateToStringConversion(new Date()));
		t2.setExpired("N");
		t2.setCounterPartyId("CP-1");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/trades/processtrades").content(mapper.writeValueAsString(t2))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string("Trade processed Successfully"));

	}
	
	@Test
	public void test_5_ProcessTradesWithLowerVersion() throws Exception {
		Trade t3 = new Trade();
		t3.setTradeId("T1");
		t3.setVersion(1);
		t3.setBookId("B1");
		t3.setMaturityDate("2020-09-28");
		t3.setCreatedDate(DateUtil.dateToStringConversion(new Date()));
		t3.setExpired("N");
		t3.setCounterPartyId("CP-1");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/trades/processtrades").content(mapper.writeValueAsString(t3))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(model().attribute("exception","Trade with lower version number is not allowed"));

	}
	
	@Test
	public void test_6_ProcessTradesWithPriorMaturity() throws Exception {
		Trade t1 = new Trade();	
		t1.setTradeId("T1");
		t1.setVersion(2);
		t1.setBookId("B1");
		t1.setMaturityDate("2020-09-02");
		t1.setCreatedDate(DateUtil.dateToStringConversion(new Date()));
		t1.setExpired("N");
		t1.setCounterPartyId("CP-1");
		mockMvc.perform(MockMvcRequestBuilders.post("/trades/processtrades").content(mapper.writeValueAsString(t1))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(model().attribute("exception", "Trade with prior maturity date is not allowed"));

	}
	
	@Test
	public void test_7_ProcessTradesWithNullTradeObject() throws Exception {
		Trade t1 = new Trade();	
		
		mockMvc.perform(MockMvcRequestBuilders.post("/trades/processtrades").content(mapper.writeValueAsString(t1))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(model().attribute("exception", "Provided Trade information is not valid"));

	}
	


}
