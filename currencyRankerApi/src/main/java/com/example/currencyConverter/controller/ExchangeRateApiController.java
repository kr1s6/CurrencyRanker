package com.example.currencyConverter.controller;

import com.example.currencyConverter.service.ExchangeRateApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller fetch rates for PLN in 166 different currencies
 */
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ExchangeRateApiController {
	@Autowired
	private ExchangeRateApiService exchangeRateApiService;

	@GetMapping("/getAllPlnJsonRates")
	public Map<String, Double> getAllPlnJsonRates() {
		return exchangeRateApiService.getAllPlnRates();
	}

	@GetMapping("/getPlnConversionRate/{currency}")
	public Double plnToEurNbpConverter(@PathVariable("currency") String currencyName) {
		return exchangeRateApiService.getRate(currencyName);
	}
}
