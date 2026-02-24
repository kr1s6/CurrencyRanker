package com.example.currencyConverter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

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

	public JsonNode getXmlObject(String url) {
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(url, String.class);
		if(response == null) {
			throw new IllegalStateException("Api returned null value. Url: " + url.substring(0, 35) + "...");
		}
		ObjectMapper jsonMapper = new ObjectMapper();
		return jsonMapper.readTree(response);
	}
}
