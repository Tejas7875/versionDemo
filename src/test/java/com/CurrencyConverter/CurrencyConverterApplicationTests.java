package com.CurrencyConverter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class CurrencyConverterApplicationTests {


	@Test
	void contextLoads()
	{

	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}




}
