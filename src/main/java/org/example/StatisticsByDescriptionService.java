package org.example;

import com.fasterxml.jackson.core.JsonPointer;

/**
 * Forms statistics for fields with name "description".
 */
public class StatisticsByDescriptionService extends AbstractStatisticsByFieldService {

    public StatisticsByDescriptionService() {
        super(new InMemoryStatisticsByFieldRepository());
    }

    public StatisticsByDescriptionService(StatisticsByFieldRepository statisticsByFieldRepository) {
        super(statisticsByFieldRepository);
    }

    public StatisticsByDescriptionService(StatisticsByFieldRepository statisticsByFieldRepository, Integer defaultPageSize) {
        super(statisticsByFieldRepository, defaultPageSize);
    }

    @Override
    public boolean isListeningForField(String fieldName, JsonPointer jsonPointer) {
        return getListenedFieldName().equals(fieldName);
    }

    @Override
    public String getListenedFieldName() {
        return "description";
    }
}
