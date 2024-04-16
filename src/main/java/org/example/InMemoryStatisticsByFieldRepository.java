package org.example;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides means of storing statistics using RAM.
 */
public class InMemoryStatisticsByFieldRepository implements StatisticsByFieldRepository{

    private final Map<String, Long> fieldValueToNumberOFItsOccurrences = new ConcurrentHashMap<>();

    @Override
    public void save(FieldValueToItsCount fToN) {
        fieldValueToNumberOFItsOccurrences.put(fToN.getFieldValue(), fToN.getNumberOfOccurrences());
    }

    @Override
    public Optional<Long> getNumberOfOccurrences(String fieldValue) {
        return Optional.ofNullable(fieldValueToNumberOFItsOccurrences.get(fieldValue));
    }

    @Override
    public List<FieldValueToItsCount> getAll(Integer pageIndex, Integer pageSize) {
        List<FieldValueToItsCount> list = fieldValueToNumberOFItsOccurrences.entrySet().stream()
                .map(entry -> new FieldValueToItsCount(entry.getKey(), entry.getValue()))
                .toList();
        try {
            if(pageSize > list.size()) {
                pageSize = list.size();
            }
            int fromIndex = pageIndex * pageSize;
            return list.subList(fromIndex, fromIndex + pageSize);
        } catch (IndexOutOfBoundsException e) {
            return Collections.emptyList();
        }

    }
}
