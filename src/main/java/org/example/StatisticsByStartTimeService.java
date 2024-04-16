package org.example;

import com.fasterxml.jackson.core.JsonPointer;

/**
 * Forms statistics for fields with name "startTime".
 */
public class StatisticsByStartTimeService extends AbstractStatisticsByFieldService {

    public StatisticsByStartTimeService() {
        super(new InMemoryStatisticsByFieldRepository());
    }

    public StatisticsByStartTimeService(StatisticsByFieldRepository statisticsByFieldRepository) {
        super(statisticsByFieldRepository);
    }

    public StatisticsByStartTimeService(StatisticsByFieldRepository statisticsByFieldRepository, Integer defaultPageSize) {
        super(statisticsByFieldRepository, defaultPageSize);
    }

    @Override
    public boolean isListeningForField(String fieldName, JsonPointer jsonPointer) {
        return getListenedFieldName().equals(fieldName);
    }

    @Override
    public String getListenedFieldName() {
        return "startTime";
    }
}
