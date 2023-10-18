package com.CurrencyConverter.controller;

import com.CurrencyConverter.Reader.PomVersionReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController
{

    @GetMapping("/version")
    public String getAppVersion() {
        String version = PomVersionReader.readVersionFromPom();
        System.out.println(version);
        return "Application Version: " + version;
    }


}
