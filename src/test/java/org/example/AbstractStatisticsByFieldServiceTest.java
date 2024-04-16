package org.example;


import com.fasterxml.jackson.core.JsonPointer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AbstractStatisticsByFieldServiceTest {
    @Mock
    StatisticsByFieldRepository byFieldRepository;

    AbstractStatisticsByFieldService byFieldService;

    @BeforeEach
    void init() {
        byFieldService = new AbstractStatisticsByFieldService(byFieldRepository) {
            @Override
            public String getListenedFieldName() {
                return "";
            }

            @Override
            public boolean isListeningForField(String fieldName, JsonPointer jsonPointer) {
                return true;
            }
        };
    }

    @Test
    void nextFieldValue_shouldSaveNewFieldValueToItsCount_ifNoValueIsMetFistTime() {
        //given
        String value = "value";
        given(byFieldRepository.getNumberOfOccurrences(any())).willReturn(Optional.empty());
        //when
        byFieldService.nextFieldValue(value);
        //then
        verify(byFieldRepository).getNumberOfOccurrences(value);
        verify(byFieldRepository).save(new FieldValueToItsCount(value, 1L));
    }

    @Test
    void nextFieldValue_shouldUpdateFieldValueToItsCount_ifValueAlreadyRegistered() {
        //given
        String value = "value";
        long valueCount = 4;
        given(byFieldRepository.getNumberOfOccurrences(any())).willReturn(Optional.of(valueCount));
        //when
        byFieldService.nextFieldValue(value);
        //then
        verify(byFieldRepository).getNumberOfOccurrences(value);
        verify(byFieldRepository).save(new FieldValueToItsCount(value, valueCount + 1));
    }

    @Test
    void nextFieldValue_shouldThrowIllegalArgumentException_ifGivenFieldValueIsNull() {
        //given
        String value = null;
        //then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> byFieldService.nextFieldValue(value));
        assertEquals("Field value can not be null", ex.getMessage());
    }

    @Test
    void getCurrentStatistics_shouldPassCorrectArgsToRepository() {
        //given
        int pageIndex = 0;
        int pageSize = 10;
        //when
        byFieldService.getCurrentStatistics(pageIndex, pageSize);
        //then
        verify(byFieldRepository).getAll(pageIndex, pageSize);
    }

    @Test
    void getCurrentStatistics_shouldReturnListWithSameItemsAsRepository() {
        //given
        int pageIndex = 0;
        int pageSize = 10;
        List<FieldValueToItsCount> expectedList = List.of(new FieldValueToItsCount("any", 1L));
        given(byFieldRepository.getAll(any(), any())).willReturn(expectedList);
        //when
        List<FieldValueToItsCount> result = byFieldService.getCurrentStatistics(pageIndex, pageSize);
        //then
        assertEquals(expectedList, result);
    }


}
