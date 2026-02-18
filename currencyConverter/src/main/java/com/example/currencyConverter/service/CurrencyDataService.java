package com.example.currencyConverter.service;

import com.example.currencyConverter.model.CurrencyData;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.currencyConverter.service.BisTurnoverService.OTC_TURNOVER;

@Service
@RequiredArgsConstructor
public class CurrencyDataService {
	@Autowired
	private ExchangeRateApiService exchangeRateApi;
	@Autowired
	private CurrencyFullNameService currencyFullName;
	public List<CurrencyData> currencyDataList;

	@PostConstruct
	private void getAllCurrencyData() {
		Map<String, Double> rates = exchangeRateApi.getAllPlnRates();
		currencyDataList = rates.entrySet().stream()
				.map(entry -> CurrencyData.builder()
						.currencyCode(entry.getKey())
						.currencyName(currencyFullName.getCurrencyName(entry.getKey()))
						.rate(entry.getValue())
						.turnover(OTC_TURNOVER.getOrDefault(entry.getKey(), -1.0))
						.build())
				.collect(Collectors.toList());
		rankCurrencies();
	}

	private void rankCurrencies() {
		currencyDataList.sort(Comparator.comparingDouble(CurrencyData::getTurnover).reversed());
		int rank = 1;
		for(CurrencyData currency : currencyDataList) {
			currency.setRank(rank++);
		}
	}

}
