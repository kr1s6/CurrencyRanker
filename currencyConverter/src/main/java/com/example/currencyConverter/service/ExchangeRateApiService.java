package com.example.currencyConverter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Exchange rates for currencies
 * <a href="https://www.exchangerate-api.com/">...</a>
 */
@Service
public class ExchangeRateApiService {
	@Value("${EXCHANGE_RATE_API}")
	private String exchangeRateApiUrl;

	public Map<String, Double> rates = new HashMap<>();

	public Double getRate(String currencyName) {
		return rates.get(currencyName);
	}

	public Map<String, Double> getAllPlnRates() {
		JsonNode json = getJsonObject(exchangeRateApiUrl);
		validateResponseBaseCode(json, "PLN");
		JsonNode conversionRates = json.get("conversion_rates");
		for(String currency : conversionRates.propertyNames()) {
			Double rate = conversionRates.get(currency).doubleValue();
			rates.put(currency, rate);
		}

		return rates;
	}

	private JsonNode getJsonObject(String url) {
		RestTemplate restTemplate = new RestTemplate();
		JsonNode json = restTemplate.getForObject(url, JsonNode.class);
		if(json == null) {
			throw new NullPointerException("Exchange rate api not available.");
		}
		return json;
	}

	private void validateResponseBaseCode(JsonNode json, String expectedBaseCode) {
		String actualBaseCode = json.get("base_code").asString();
		if(!actualBaseCode.equals(expectedBaseCode)) {
			throw new RuntimeException("Wrong currency converter!");
		}
	}
}
