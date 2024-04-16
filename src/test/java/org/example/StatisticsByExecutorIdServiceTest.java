package org.example;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class StatisticsByExecutorIdServiceTest {
    StatisticsByExecutorIdService service = new StatisticsByExecutorIdService(mock(StatisticsByFieldRepository.class));

    @Test
    void isListeningForField_shouldReturnTrueToExecutionIdFieldOfAnyDepthOrObject() {
        //given
        String fieldName = "executorId";
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
}
