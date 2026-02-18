package com.example.currencyConverter.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CurrencyData {
	private String currencyName;
	private String currencyCode;
	private double rate;
	private double turnover;
	private Integer rank;
	//private double score;
}
