package org.example;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatisticsByGuarantorEmailsTest {

    StatisticsByGuarantorEmails service;

    @Mock
    StatisticsByFieldRepository repository;

    @BeforeEach
    void init() {
        service= new StatisticsByGuarantorEmails(repository);
    }

    @Test
    void isListeningForField_shouldReturnTrueToGuarantorEmailsFieldOfAnyDepthOrObject() {
        //given
        String fieldName = "guarantorEmails";
        JsonPointer jsonPointer = JsonPointer.compile("/0/any/0/" + fieldName);
        //when
        boolean result = service.isListeningForField(fieldName, jsonPointer);
        //then
        assertTrue(result, "Should return true to field name of any depth or object.");
    }

    @Test
    void isListeningForField_shouldReturnFalse_ifGivenFieldNameIsWrong() {
        //given
        String fieldName = "field";
        JsonPointer jsonPointer = JsonPointer.compile("/0/any/0/" + fieldName);
        //when
        boolean result = service.isListeningForField(fieldName, jsonPointer);
        //then
        assertFalse(result, "Should return false to other fields.");
    }

    @Test
    void isListeningForField_shouldReturnFalse_ifGivenFieldNameIsNull() {
        //given
        String fieldName = null;
        JsonPointer jsonPointer = JsonPointer.compile("/0/any/0/field");
        //when
        boolean result = service.isListeningForField(fieldName, jsonPointer);
        //then
        assertFalse(result, "Should return false to null field names.");
    }

    @Test
    void nextFieldValue_shouldSplitGivenStringByCommaSpaceRegexAndPassEveryItemToRepository() {
        //given
        String emails = "some@email.com, some1@email.com, some2@email.com";
        //when
        service.nextFieldValue(emails);
        //then
        verify(repository).save(new FieldValueToItsCount("some@email.com", 1L));
        verify(repository).save(new FieldValueToItsCount("some1@email.com", 1L));
        verify(repository).save(new FieldValueToItsCount("some2@email.com", 1L));
    }

    @Test
    void nextFieldValue_shouldThrowIllegalArgumentException_ifGivenValueIsNull() {
        //given
        String emails = null;
        //then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.nextFieldValue(emails));
        assertEquals("Field value can not be null", ex.getMessage());
    }

}
