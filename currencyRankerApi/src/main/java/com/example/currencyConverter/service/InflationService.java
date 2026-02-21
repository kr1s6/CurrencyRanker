package com.example.currencyConverter.service;

import com.example.currencyConverter.model.InflationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InflationService {
	@Autowired
	private FetchDataService fetchData;

	private final Map<String, String> EUROSTAT_CURRENCY_MAP = Map.ofEntries(
			Map.entry("EUR", "EA"), Map.entry("PLN", "PL"),
			Map.entry("CZK", "CZ"), Map.entry("HUF", "HU"),
			Map.entry("SEK", "SE"), Map.entry("NOK", "NO"),
			Map.entry("DKK", "DK"), Map.entry("CHF", "CH"),
			Map.entry("RON", "RO"), Map.entry("BGN", "BG"),
			Map.entry("HRK", "HR"), Map.entry("ISK", "IS")
	);

	/**
	 * Get inflation and inflation last updated date.
	 * @return Map of InflationDto for European cuntries.
	 */
	public Map<String, InflationDto> getMonthlyEuropeanCountriesInflation() {
		Map<String, InflationDto> inflationMap = new HashMap<>();
		for(Map.Entry<String, String> country : EUROSTAT_CURRENCY_MAP.entrySet()) {
			String code = country.getKey();
			String countryCode = country.getValue();
			InflationDto inflation = getMonthlyInflation(code, countryCode);
			inflationMap.put(code, inflation);
		}

		return inflationMap;
	}

	private InflationDto getMonthlyInflation(String code, String country) {
		String euroStatUrl = "https://ec.europa.eu/eurostat/api/dissemination/statistics/1.0/data/prc_hicp_manr?geo=" + country + "&coicop=CP00&format=JSON&lang=en&lastTimePeriod=1";
		JsonNode json = fetchData.getJsonObject(euroStatUrl);

		String label = json.get("label").asString();
		String geo = json.get("dimension").get("geo").get("category").get("label").toString();
		if(!geo.contains(country)) {
			throw new IllegalStateException("Potentially wrong data for HICP - " + geo + " inflation. Unexpected geo: " + geo);
		}
		if(!label.contains("HICP - monthly data")) {
			throw new IllegalStateException("Potentially wrong data for HICP - " + geo + " inflation. Unexpected label: " + label);
		}

		String updated = json.get("updated").toString().replace("\"", "").split("T")[0];
		Double value = json.get("value").get("0").doubleValue();
		return InflationDto.builder()
				.code(code)
				.inflation(value)
				.update(updated)
				.build();
	}

}


