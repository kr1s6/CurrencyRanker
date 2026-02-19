package com.example.currencyConverter.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CurrencyBasicInfoService {

	public String getCurrencyName(String code) {
		try {
			Currency currency = Currency.getInstance(code);
			return currency.getDisplayName().toUpperCase();
		} catch(IllegalArgumentException e) {
			return code;
		}
	}

	public Map<String, List<String>> getCurrencyCountries() {
		Map<String, HashSet<String>> currencyCountries = new HashMap<>();

		for(Locale locale : Locale.getAvailableLocales()) {
			try {
				Currency currency = Currency.getInstance(locale);
				String currencyCode = currency.getCurrencyCode();
				String country = locale.getDisplayCountry();
				if(!country.isEmpty()) {
					currencyCountries
							.computeIfAbsent(currencyCode, k -> new HashSet<>())
							.add(country);
				}
			} catch(IllegalArgumentException e) {
				//	Ignore locale without currencies
			}
		}
		return formatCurrencyCountries(currencyCountries);
	}

	private Map<String, List<String>> formatCurrencyCountries(Map<String, HashSet<String>> currencyCountries) {
		Map<String, List<String>> formattedToString = new HashMap<>();

		currencyCountries.forEach((code, countries) -> {
			formattedToString.put(code, countries.stream().toList());
		});

		return formattedToString;
	}

}
