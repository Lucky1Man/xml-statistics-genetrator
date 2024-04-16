package org.example;

import java.util.List;

/**
 * This class is used to form statistics by field values.
 */
public interface StatisticsByFieldService extends FieldListener {

    /**
     * Is used to get most relevant statistics. What statistics is relevant depends on implementation.
     * @return relevant statistics
     */
    List<FieldValueToItsCount> getCurrentStatistics();

    /**
     * Is used to get statistics for page with given index and size.
     * @param pageIndex index of page
     * @param pageSize size of page
     * @return page with given index and size
     */
    List<FieldValueToItsCount> getCurrentStatistics(Integer pageIndex, Integer pageSize);

}
