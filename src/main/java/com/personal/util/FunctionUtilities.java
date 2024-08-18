package com.personal.util;

import jakarta.enterprise.context.ApplicationScoped;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

@ApplicationScoped
public class FunctionUtilities {

    public String currentTime(String format){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }

    public String decimalFormat(String amount, String decimalFormat){
        DecimalFormat df = new DecimalFormat(decimalFormat);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(Double.parseDouble(amount));
    }

    public String leftFill(String value, Character gap, int limit){
        value = value.trim();
        if(value.length()>=limit)
            return value;
        int delta = limit-value.length();
        StringBuilder trama = new StringBuilder();
        IntStream.range(0, delta).forEach(i -> trama.append(gap));
        trama.append(value);
        return trama.toString();
    }

    public String rightFill(String value, Character gap, int limit){
        value = value.trim();
        if(value.length()>=limit)
            return value;
        int delta = limit-value.length();
        StringBuilder trama = new StringBuilder(value);
        IntStream.range(0, delta).forEach(i -> trama.append(gap));
        return trama.toString();
    }

    public String espacios(String value){
        return value.trim();
    }

    public String upperCase(String value){
        return value.toUpperCase();
    }

    public String lowerCase(String value){
        return value.toLowerCase();
    }

}
