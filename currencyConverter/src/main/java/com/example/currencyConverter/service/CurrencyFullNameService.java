package com.example.currencyConverter.service;

import org.springframework.stereotype.Service;

import java.util.Currency;

@Service
public class CurrencyFullNameService {

	public String getCurrencyName(String code) {
		try {
			Currency currency = Currency.getInstance(code);
			return currency.getDisplayName().toUpperCase();
		} catch(IllegalArgumentException e) {
			return code;
		}
	}
}
