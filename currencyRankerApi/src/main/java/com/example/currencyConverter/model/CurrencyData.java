package com.example.currencyConverter.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class CurrencyData {
	private String currencyName;
	private String currencyCode;
	private double rate;
	private double turnover;
	private Integer rank;
	private List<String> countries;
	//private double score;
}
