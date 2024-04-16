package org.example;

import java.util.List;
import java.util.Optional;

/**
 * Repository class which is used to store statistics of current request for statistics.
 */
public interface StatisticsByFieldRepository {

    /**
     * Saves given statistics piece.
     * @param fToN statistics piece to be saved
     */
    void save(FieldValueToItsCount fToN);

    /**
     * Is used to get number of occurrences of specified field value.
     * @param fieldValue field value to be used for search
     * @return number of occurrences of given value
     */
    Optional<Long> getNumberOfOccurrences(String fieldValue);

    /**
     * Is used to get current statistics in safe way.
     * @param pageIndex index of page to be returned
     * @param pageSize size of page to be returned
     * @return page of statistics of specified index and size
     */
    List<FieldValueToItsCount> getAll(Integer pageIndex, Integer pageSize);

}
