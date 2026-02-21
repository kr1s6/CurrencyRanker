package com.example.currencyConverter.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InflationDto {
	private String code;
	private Double inflation;
	private String update;
}
