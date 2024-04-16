package org.example;

import com.fasterxml.jackson.core.JsonPointer;

/**
 * Forms statistics for fields with name "finishTime".
 */
public class StatisticsByFinishTimeService extends AbstractStatisticsByFieldService {

    public StatisticsByFinishTimeService() {
        super(new InMemoryStatisticsByFieldRepository());
    }

    public StatisticsByFinishTimeService(StatisticsByFieldRepository statisticsByFieldRepository) {
        super(statisticsByFieldRepository);
    }

    public StatisticsByFinishTimeService(StatisticsByFieldRepository statisticsByFieldRepository, Integer defaultPageSize) {
        super(statisticsByFieldRepository, defaultPageSize);
    }

    @Override
    public boolean isListeningForField(String fieldName, JsonPointer jsonPointer) {
        return getListenedFieldName().equals(fieldName);
    }

    @Override
    public String getListenedFieldName() {
        return "finishTime";
    }
}
