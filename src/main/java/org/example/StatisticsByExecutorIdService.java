package org.example;

import com.fasterxml.jackson.core.JsonPointer;

/**
 * Forms statistics for fields with name "executorId".
 */
public class StatisticsByExecutorIdService extends AbstractStatisticsByFieldService {

    public StatisticsByExecutorIdService() {
        super(new InMemoryStatisticsByFieldRepository());
    }

    public StatisticsByExecutorIdService(StatisticsByFieldRepository statisticsByFieldRepository) {
        super(statisticsByFieldRepository);
    }

    public StatisticsByExecutorIdService(StatisticsByFieldRepository statisticsByFieldRepository, Integer defaultPageSize) {
        super(statisticsByFieldRepository, defaultPageSize);
    }

    @Override
    public boolean isListeningForField(String fieldName, JsonPointer jsonPointer) {
        return getListenedFieldName().equals(fieldName);
    }

    @Override
    public String getListenedFieldName() {
        return "executorId";
    }
}
