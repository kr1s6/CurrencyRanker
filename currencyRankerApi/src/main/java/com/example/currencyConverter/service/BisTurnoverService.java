package com.example.currencyConverter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <a href="https://www.bis.org/statistics/rpfx25_fx.htm">OTC foreign exchange turnover by currency 2025</a>
 */
@Service
@RequiredArgsConstructor
public class BisTurnoverService {

	/**
	 * Plynność walut
	 * Rzeczywiste wykorzystanie waluty
	 * Trading Volume (Wolumen handlu)
	 * OTC foreign exchange turnover by currency 2025
	 */
	public static final Map<String, Double> OTC_TURNOVER = Map.ofEntries(
			Map.entry("USD", 89.2),
			Map.entry("EUR", 28.9),
			Map.entry("JPY", 16.8),
			Map.entry("GBP", 10.2),
			Map.entry("CNY", 8.5),
			Map.entry("CHF", 6.4),
			Map.entry("AUD", 6.1),
			Map.entry("CAD", 5.8),
			Map.entry("HKD", 3.8),
			Map.entry("SGD", 2.4),
			Map.entry("INR", 1.9),
			Map.entry("KRW", 1.8),
			Map.entry("SEK", 1.6),
			Map.entry("MXN", 1.6),
			Map.entry("NZD", 1.5),
			Map.entry("NOK", 1.3),
			Map.entry("TWD", 1.2),
			Map.entry("BRL", 0.9),
			Map.entry("ZAR", 0.8),
			Map.entry("PLN", 0.8),
			Map.entry("DKK", 0.7),
			Map.entry("IDR", 0.7),
			Map.entry("TRY", 0.5),
			Map.entry("THB", 0.5),
			Map.entry("ILS", 0.4),
			Map.entry("HUF", 0.4),
			Map.entry("CZK", 0.4),
			Map.entry("CLP", 0.3),
			Map.entry("PHP", 0.2),
			Map.entry("COP", 0.2),
			Map.entry("MYR", 0.2),
			Map.entry("AED", 0.1),
			Map.entry("SAR", 0.1),
			Map.entry("RON", 0.1),
			Map.entry("PEN", 0.1),
			Map.entry("BGN", 0.0),
			Map.entry("RUB", 0.0),
			Map.entry("ARS", 0.0),
			Map.entry("BHD", 0.0)
	);
}
