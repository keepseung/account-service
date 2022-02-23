package com.payservice.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 사용자에게 문자열로 입력받은 날짜를 LocalDate 형식으로 바꿔주는 컨버터
 */
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private final DateTimeFormatter formatter;

    public StringToLocalDateConverter(String dateFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public LocalDate convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        return LocalDate.parse(source, formatter);
    }
}
