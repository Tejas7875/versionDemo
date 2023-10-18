package com.CurrencyConverter.controller;

import com.CurrencyConverter.controller.CurrencyConverterController;
import com.CurrencyConverter.entity.CurrencyConversionRecord;
import com.CurrencyConverter.jpa.CurrencyConversionRecordRepository;
import com.CurrencyConverter.model.ExchangeRates;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CurrencyConverterControllerTest {

    @Test
    public void testIndex() {
        CurrencyConverterController controller = new CurrencyConverterController();
        String result = controller.index();
        assertEquals("currency_converter", result);
    }

    @Autowired
    private CurrencyConversionRecordRepository recordRepository;


    @Test
    public void testFetchExchangeRates() {
        CurrencyConverterController controller = new CurrencyConverterController();
        String date = "2023-10-10";
        Model model = mock(Model.class);

        String result = controller.fetchExchangeRates(date, model);
        System.out.println(result);
        // You can add assertions here to verify the model attributes and return view
    }

    @Test
    public void testEnterDate() {
        CurrencyConverterController controller = new CurrencyConverterController();
        String result = controller.enterDate();
        assertEquals("date", result);
    }

}
