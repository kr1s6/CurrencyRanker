package com.example.currencyConverter.controller;

import com.example.currencyConverter.model.CurrencyData;
import com.example.currencyConverter.service.CurrencyDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class CurrencyDataController {

	@Autowired
	private final CurrencyDataService currencyDataService;

	@GetMapping("/getAllCurrencyData")
	public List<CurrencyData> getAllCurrencyData() {
		return currencyDataService.currencyDataList;
	}
}
