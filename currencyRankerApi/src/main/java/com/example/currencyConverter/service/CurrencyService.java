package com.example.currencyConverter.service;

import com.example.currencyConverter.model.CurrencyEntity;
import com.example.currencyConverter.model.InflationDto;
import com.example.currencyConverter.repository.CurrencyRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.currencyConverter.service.TurnoverService.OTC_TURNOVER;

@Service
@RequiredArgsConstructor
public class CurrencyService {
	@Autowired
	private CurrencyRepository currencyRepository;
	@Autowired
	private ExchangeRateApiService exchangeRateApi;
	@Autowired
	private CurrencyBasicInfoService currencyBasicInfo;
	@Autowired
	private InflationService inflationService;

	@PostConstruct
	private void initCurrenciesToDatabase() {
		Map<String, String> currencyCodeWithNameMap = exchangeRateApi.getAllCurrenciesCodeAndName();
		Map<String, Double> plnRates = exchangeRateApi.getAllPlnRates();
		Map<String, List<String>> currencyCountries = currencyBasicInfo.getCurrencyCountries();
		Map<String, InflationDto> euCurrenciesInflation = inflationService.getMonthlyEuropeanCountriesInflation();

		List<CurrencyEntity> currencyEntityList = currencyCodeWithNameMap.entrySet().stream()
				.map(entry -> {
							String code = entry.getKey();
							String name = entry.getValue();
							Double rate = plnRates.get(code);
							Double turnover = OTC_TURNOVER.getOrDefault(code, -1.0);
							List<String> countries = currencyCountries.getOrDefault(code, List.of("UNKNOWN"));
							InflationDto inflationDto = euCurrenciesInflation.getOrDefault(code, null);

							return CurrencyEntity.builder()
									.currencyCode(code)
									.currencyName(name)
									.rate(rate)
									.turnover(turnover)
									.countries(countries)
									.inflation(inflationDto != null ? inflationDto.getInflation() : null)
									.inflationUpdate(inflationDto != null ? inflationDto.getUpdate() : null)
									.build();
						}
				).collect(Collectors.collectingAndThen(
						Collectors.toList(),
						this::rankCurrencies));

		currencyRepository.saveAll(currencyEntityList);
	}

	private List<CurrencyEntity> rankCurrencies(List<CurrencyEntity> currencyEntityList) {
		currencyEntityList.sort(Comparator.comparingDouble(CurrencyEntity::getTurnover).reversed());
		int rank = 1;
		for(CurrencyEntity currency : currencyEntityList) {
			currency.setRank(rank++);
		}
		return currencyEntityList;
	}

}
