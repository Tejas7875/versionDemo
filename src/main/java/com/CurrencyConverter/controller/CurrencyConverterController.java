
package com.CurrencyConverter.controller;

import com.CurrencyConverter.jpa.CurrencyConversionRecordRepository;
import com.CurrencyConverter.entity.CurrencyConversionRecord;
import com.CurrencyConverter.model.CustomExchangeRates;
import com.CurrencyConverter.model.ExchangeRates;
import com.CurrencyConverter.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class CurrencyConverterController {
    @Autowired
    private CurrencyConversionService conversionService;

    @Autowired
    private CurrencyConversionRecordRepository recordRepository;

   // @Autowired
    private WebProperties.LocaleResolver localeResolver;

    String language;

//    @Value("${app.version}")
//    private String appVersion;
//
//    @GetMapping("/version")
//    public String getAppVersion() {
//        return "Application Version: " + appVersion;
//    }


    @GetMapping("/")
    public String index() {
        return "currency_converter";
    }

    @GetMapping("/convert")
    public String convertCurrency(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String amount,
            Model model
    ) {
        System.out.println("From " + from + " to " + to + " amount " + amount);

        ExchangeRates exchangeRates = conversionService.getExchangeRates(from, to);

        System.out.println("Exchange Rates : " + exchangeRates);

        double exchangeRate = exchangeRates.getData().get(to).getValue();
        System.out.println("Exchange Rate in double format - " + exchangeRate);
        double amountVerified;
        try {
            amountVerified = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            // Handle the case where 'amount' is not a valid double
            return "error_page"; // You can create an error page to display a user-friendly error message
        }
        double convertedAmount = amountVerified * exchangeRate;

        System.out.println("test");
        model.addAttribute("fromCurrency", from);
        model.addAttribute("toCurrency", to);
        model.addAttribute("amount", amount);
        model.addAttribute("convertedAmount", convertedAmount);

        // Save the conversion record with the convertedAmount
        CurrencyConversionRecord conversionRecord = new CurrencyConversionRecord(from, to, amountVerified, convertedAmount);
        recordRepository.save(conversionRecord);

        return "currency_converter";
    }


    @PostMapping("/processDate")
    public String fetchExchangeRates(@RequestParam String date, Model model) {
        // Construct the API URL

        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            // If the format is not correct, return an error page
            return "error_page";
        }


        //Entered date
        // Extract year, month, and day from the 'date' parameter
        String[] dateParts = date.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);



        LocalDate inputDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Compare the input date with the current date
        if (inputDate.isAfter(currentDate) || inputDate.isEqual(currentDate)) {
            // The input date is greater than or equal to the current date
            return "invalid_date";
        }

        // Check if the date values are valid
        if (month < 1 || month > 12 || day < 1 || day > 31) {
            // If any of the conditions are not met, return an error page
            return "error_page";
        }

        String apiUrl = "https://api.currencyapi.com/v3/historical?date=" + date + "&apikey=cur_live_CG4wGykf8g7z2s9xSScY6aCa6vmibfN4KxTLwRgk";

        System.out.println("Date is " + date);

        // Use RestTemplate to make the API request and fetch the data
        RestTemplate restTemplate = new RestTemplate();
        ExchangeRates exchangeRates = restTemplate.getForObject(apiUrl, ExchangeRates.class);

        // Create a new CustomExchangeRates object to hold the required data
        CustomExchangeRates customExchangeRates = new CustomExchangeRates();
        customExchangeRates.setLast_updated_at(exchangeRates.getMeta().getLast_updated_at());

        Map<String, Double> exchangeRateMap = new HashMap<>();
        for (Map.Entry<String, ExchangeRates.CurrencyData> entry : exchangeRates.getData().entrySet()) {
            exchangeRateMap.put(entry.getKey(), entry.getValue().getValue());
        }
        customExchangeRates.setExchangeRates(exchangeRateMap);

        // Add the customExchangeRates object to the model
        model.addAttribute("customExchangeRates", customExchangeRates);

        return "currency_exchange"; // Display the exchange rates in the currency_exchange.jsp
    }


    @GetMapping("/date")
    public String enterDate() {
        return "date"; // This will redirect to date.jsp
    }

    @GetMapping("/transactionRecords")
    public String getTransactionRecords(
            Model model,
            @RequestParam(name = "lang", required = false) String lang,
            HttpServletRequest request
    ) {

        System.out.println("lang is " + lang);
        List<CurrencyConversionRecord> transactionRecords = recordRepository.findAll();
        model.addAttribute("transactionRecords", transactionRecords);
        return "transaction_records";
    }

    @PostMapping("/changeLang")
    public String changeLang(@RequestParam String lang, HttpServletRequest request, HttpServletResponse response) {

        System.out.println( "Change lang " + lang);
        request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale(lang));


        return "redirect:" + request.getHeader("Referer");
    }
}
