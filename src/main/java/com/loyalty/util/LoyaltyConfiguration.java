package com.loyalty.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@Data
@PropertySource("classpath:loyalty.properties")
public class LoyaltyConfiguration {
	@Value("${slab_1}")
	private Long slab1;
	@Value("${slab_2}")
	private Long slab2;
	@Value("${points_per_slab_1}")
	private Long rateSlab1;
	@Value("${points_per_slab_2}")
	private Long rateSlab2;
}
