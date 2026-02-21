package com.example.currencyConverter.controller;

import com.example.currencyConverter.model.CurrencyEntity;
import com.example.currencyConverter.repository.CurrencyRepository;
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
	private CurrencyRepository currencyRepository;

	@GetMapping("/getAllCurrencyData")
	public List<CurrencyEntity> getAllCurrencyData() {
		return currencyRepository.findAll();
	}
}
