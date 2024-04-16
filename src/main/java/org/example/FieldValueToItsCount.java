package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class which is used to store statistics piece.
 */
@Data
@AllArgsConstructor
public class FieldValueToItsCount {
    private final String fieldValue;
    private final Long numberOfOccurrences;
}
