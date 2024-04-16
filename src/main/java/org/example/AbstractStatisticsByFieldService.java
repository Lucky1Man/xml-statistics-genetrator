package org.example;

import java.util.Collections;
import java.util.List;

/**
 * Provides base for StatisticsByFieldService implementation.
 */
public abstract class AbstractStatisticsByFieldService implements StatisticsByFieldService {

    protected final StatisticsByFieldRepository statisticsByFieldRepository;

    private final Integer defaultPageSize;


    protected AbstractStatisticsByFieldService(StatisticsByFieldRepository statisticsByFieldRepository) {
        this(statisticsByFieldRepository, Integer.MAX_VALUE);
    }

    protected AbstractStatisticsByFieldService(StatisticsByFieldRepository statisticsByFieldRepository,
                                               Integer defaultPageSize) {
        this.statisticsByFieldRepository = statisticsByFieldRepository;
        this.defaultPageSize = defaultPageSize;
    }

    @Override
    public void nextFieldValue(String fieldValue) {
        if(fieldValue == null) {
            throw new IllegalArgumentException("Field value can not be null");
        }
        statisticsByFieldRepository.save(new FieldValueToItsCount(
                fieldValue,
                statisticsByFieldRepository.getNumberOfOccurrences(fieldValue).orElse(0L) + 1
        ));
    }

    @Override
    public List<FieldValueToItsCount> getCurrentStatistics() {
        return getCurrentStatistics(0, defaultPageSize);
    }

    @Override
    public List<FieldValueToItsCount> getCurrentStatistics(Integer pageIndex, Integer pageSize) {
        return Collections.unmodifiableList(statisticsByFieldRepository.getAll(pageIndex, pageSize));
    }


}
