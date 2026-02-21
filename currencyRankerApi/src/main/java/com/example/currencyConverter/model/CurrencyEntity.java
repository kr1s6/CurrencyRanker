package com.example.currencyConverter.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency")
public class CurrencyEntity {
	@Id
	@Column(name = "currency_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", unique = true)
	private String currencyName;

	@Column(name = "code", unique = true)
	private String currencyCode;

	@Column(name = "rate")
	private double rate;

	@Column(name = "turnover")
	private double turnover;

	@Column(name = "rank")
	private Integer rank;

	@Column(name = "countries")
	private List<String> countries;

	@Column(name = "inflation")
	private Double inflation;

	@Column(name = "inflation_update")
	private String inflationUpdate;

}
