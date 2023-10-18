package com.CurrencyConverter.service;

import com.CurrencyConverter.service.CurrencyConversionService;
import com.CurrencyConverter.model.ExchangeRates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrencyConversionServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private CurrencyConversionService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new CurrencyConversionService(restTemplate);
    }

}
