package org.example;

import com.fasterxml.jackson.core.JsonPointer;

import java.util.Arrays;

/**
 * Forms statistics for fields with name "guarantorEmails".
 */
public class StatisticsByGuarantorEmails extends AbstractStatisticsByFieldService {

    public StatisticsByGuarantorEmails() {
        super(new InMemoryStatisticsByFieldRepository());
    }

    public StatisticsByGuarantorEmails(StatisticsByFieldRepository statisticsByFieldRepository) {
        super(statisticsByFieldRepository);
    }

    public StatisticsByGuarantorEmails(StatisticsByFieldRepository statisticsByFieldRepository, Integer defaultPageSize) {
        super(statisticsByFieldRepository, defaultPageSize);
    }

    @Override
    public boolean isListeningForField(String fieldName, JsonPointer jsonPointer) {
        return getListenedFieldName().equals(fieldName);
    }

    @Override
    public void nextFieldValue(String fieldValue) {
        if(fieldValue == null) {
            throw new IllegalArgumentException("Field value can not be null");
        }
        Arrays.stream(fieldValue.split(", ")).forEach(super::nextFieldValue);
    }

    @Override
    public String getListenedFieldName() {
        return "guarantorEmails";
    }
}
