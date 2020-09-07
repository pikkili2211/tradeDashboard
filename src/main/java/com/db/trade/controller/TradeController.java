package com.db.trade.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.db.trade.model.Trade;
import com.db.trade.service.TradeService;

@RequestMapping("/trades")
@EnableScheduling
@RestController
public class TradeController {

	@Autowired
	TradeService tradeService;	

	@RequestMapping(value = "/alltrades", method = RequestMethod.GET)
	public ModelAndView allTrades(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		List<Trade> trades = tradeService.getAllTrades();

		ModelAndView view = new ModelAndView();
		view.setViewName("trades");
		model.put("trades", trades);

		return view;
	}

	@RequestMapping(value = "/processtrades", consumes = {"application/*"}, method = { RequestMethod.POST, RequestMethod.PUT }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity processTrades(@Valid @RequestBody Trade trade, BindingResult result, Model model) throws Exception {
      String status;
      
      if(result.hasErrors()) {
    	  throw new Exception("Provided Trade information is not valid");
      }
		try {
			status = tradeService.processTrades(trade);
		} catch (Exception e) {
			if(null != e.getMessage() )
				throw new Exception(e.getMessage());
			else
				 throw new Exception("Issue while processing the trade");
		}
		return new ResponseEntity<>(status, HttpStatus.OK);

	}

	// @Scheduled(cron = "0 0 24 * * ?")
	@Scheduled(fixedDelay = 100000)
	public void tradeExpiryCheck() {
		tradeService.updateTradeExpiry();
	}

}
