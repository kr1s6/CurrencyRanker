package com.example.currencyConverter.service;

import com.example.currencyConverter.model.InflationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.*;

/**
 * <a href="https://data-explorer.oecd.org">OECD CPI</a>
 */
@Service
@RequiredArgsConstructor
public class InflationService {
	@Autowired
	private FetchDataService fetchData;
	private final Map<String, InflationDto> inflationMap = new HashMap<>();
	private final Map<String, String> EUROSTAT_CURRENCY_MAP = Map.ofEntries(
			Map.entry("EUR", "EA"), Map.entry("PLN", "PL"),
			Map.entry("CZK", "CZ"), Map.entry("HUF", "HU"),
			Map.entry("SEK", "SE"), Map.entry("NOK", "NO"),
			Map.entry("DKK", "DK"), Map.entry("CHF", "CH"),
			Map.entry("RON", "RO"), Map.entry("BGN", "BG"),
			Map.entry("HRK", "HR"), Map.entry("ISK", "IS")
	);
	private final List<InflationDto> MANUAL_INFLATION_LIST = List.of(
			new InflationDto("USD", 2.39, "2026-01"),
			new InflationDto("JPY", 1.5, "2026-01"),
			new InflationDto("GBP", 3.20, "2026-01"),
			new InflationDto("CNY", 0.2, "2026-01"),
			new InflationDto("AUD", 3.8, "2025-12"),
			new InflationDto("CAD", 2.29, "2026-01"),
			new InflationDto("HKD", 1.4, "2025-12"),
			new InflationDto("SGD", 1.2, "2025-12"),
			new InflationDto("INR", 2.75, "2026-01"),
			new InflationDto("KRW", 2.01, "2026-01"),
			new InflationDto("MXN", 3.79, "2026-01"),
			new InflationDto("NZD", 3.1, "2025-12"),
			new InflationDto("TWD", 0.69, "2026-01"),
			new InflationDto("BRL", 4.44, "2026-01"),
			new InflationDto("ZAR", 3.5, "2026-01"),
			new InflationDto("COP", 5.35, "2026-01"),
			new InflationDto("ILS", 1.77, "2026-01"),
			new InflationDto("ARS", 32.41, "2026-01"),
			new InflationDto("RUB", 6.0, "2026-01"),
			new InflationDto("BHD", 0.5, "2025-12"),
			new InflationDto("FJD", -2.5, "2026-01"),
			new InflationDto("SCR", null, null),
			new InflationDto("TVD", 3.8, "2025-01"),
			new InflationDto("CDF", 2.1, "2025-11"),
			new InflationDto("BBD", 1.7, "2025-11"),
			new InflationDto("GTQ", 0.96, "2026-01"),
			new InflationDto("HNL", 4.23, "2026-01"),
			new InflationDto("UGX", 3.2, "2026-01"),
			new InflationDto("TND", 4.8,  "2026-01"),
			new InflationDto("STN", 10.3, "2025-12"),
			new InflationDto("SLE", null, null),
			new InflationDto("BSD", 1.2, "2025-07"),
			new InflationDto("SDG", null, null)
	);

	public Map<String, InflationDto> getInflationMap() {
		setEurostatCountriesInflation();
		setInflationManually();
		return inflationMap;
	}

	/**
	 * Set inflation for currencies outside EU and inflation last updated date.
	 */
	private void setInflationManually() {
		for(InflationDto dto : MANUAL_INFLATION_LIST){
			inflationMap.put(dto.getCode(), dto);
		}
	}

	/**
	 * Set inflation from the European countries and inflation last updated date.
	 */
	public void setEurostatCountriesInflation() {
		for(Map.Entry<String, String> country : EUROSTAT_CURRENCY_MAP.entrySet()) {
			String code = country.getKey();
			String countryCode = country.getValue();
			InflationDto inflationDto = parseEurostatJson(code, countryCode);
			inflationMap.put(code, inflationDto);
		}
	}

	private InflationDto parseEurostatJson(String code, String country) {
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

	//INFO - Postponed
	private void getOecdCountriesInflation() {
		String OecdUrl2026_01 = "https://sdmx.oecd.org/public/rest/data/OECD.SDD.TPS,DSD_PRICES@DF_PRICES_ALL,1.0/.M.N.CPI.PA._T.N.GY+_Z?startPeriod=2026-01&dimensionAtObservation=AllDimensions";
		JsonNode json = fetchData.getXmlObject(OecdUrl2026_01);
		List<String> countryCodes = new ArrayList<>();

		JsonNode values = json
				.get("structure")
				.get("dimensions")
				.get("observation")
				.get(0)
				.get("values");

		for(JsonNode country : values) {
			String countryCode = country.get("id").asString();
			Currency currency = Currency.getInstance(new Locale("", countryCode));
			String currencyCode = currency.getCurrencyCode();
			countryCodes.add(countryCode);
		}
	}

}


