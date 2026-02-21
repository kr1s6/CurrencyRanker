package com.example.currencyConverter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

@Service
public class FetchDataService {

	public JsonNode getJsonObject(String url) {
		RestTemplate restTemplate = new RestTemplate();
		JsonNode json = restTemplate.getForObject(url, JsonNode.class);
		if(json == null) {
			throw new IllegalStateException("Api returned null value. Url: " + url.substring(0, 35) + "...");
		}
		return json;
	}
}
