package com.example.currencyConverter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provides rates and actual currencies codes and names.
 * <a href="https://www.exchangerate-api.com/">www.exchangerate-api.com</a>
 */
@Service
@RequiredArgsConstructor
public class ExchangeRateApiService {
	@Autowired
	private FetchDataService fetchDataService;
	@Value("${EXCHANGE_RATE_API_KEY}")
	public String EXCHANGE_RATE_API_KEY;
	private final Map<String, Double> rates = new HashMap<>();

	/**
	 * Get all currencies code and name.
	 */
	public Map<String, String> getAllCurrenciesCodeAndName() {
		String exchangeRateApiCodes = "https://v6.exchangerate-api.com/v6/" + EXCHANGE_RATE_API_KEY + "/codes";
		JsonNode json = fetchDataService.getJsonObject(exchangeRateApiCodes);
		JsonNode supportedCurrencies = json.get("supported_codes");

		Map<String, String> currencyCodeWithNameMap = new HashMap<>();
		//INFO - SLL & ZWG is only for historical data.
		Set<String> oldCurrencies = Set.of("SLL", "ZWG");
		for(JsonNode currency : supportedCurrencies) {
			String code = currency.get(0).asString();
			String name = currency.get(1).asString();
			if(oldCurrencies.contains(code)) continue;

			currencyCodeWithNameMap.put(code, name);
		}
		return currencyCodeWithNameMap;
	}

	public Double getRate(String currencyName) {
		return rates.get(currencyName);
	}

	//TODO dodać rates dla TOP 1 waluty czyli pewnie USD
	public Map<String, Double> getAllPlnRates() {
		String url = "https://v6.exchangerate-api.com/v6/" + EXCHANGE_RATE_API_KEY + "/latest/PLN";
		JsonNode json = fetchDataService.getJsonObject(url);
		validateResponseBaseCode(json, "PLN");
		JsonNode conversionRates = json.get("conversion_rates");
		for(String code : conversionRates.propertyNames()) {
			Double rate = conversionRates.get(code).doubleValue();
			rates.put(code, rate);
		}

		return rates;
	}

	private void validateResponseBaseCode(JsonNode json, String expectedBaseCode) {
		String actualBaseCode = json.get("base_code").asString();
		if(!actualBaseCode.equals(expectedBaseCode)) {
			throw new RuntimeException("Wrong currency converter!");
		}
	}
}
