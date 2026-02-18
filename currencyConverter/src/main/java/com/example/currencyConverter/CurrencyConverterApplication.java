package com.example.currencyConverter;

import com.example.currencyConverter.service.ExchangeRateApiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurrencyConverterApplication {
	public static void main(String[] args) {
		SpringApplication.run(CurrencyConverterApplication.class, args);
	}

//	TODO zrób wykresy gdzie PLN jest więcej warte od innych walut i dodaj memey że jak jest więcej warte
//	 to POLSKA GUROM a jak jest mniej warte to sadeq

}
