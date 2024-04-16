package org.example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Representation of main structure.
 */
public class ExecutionFact {
    private UUID executorId;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private List<String> guarantorEmails;
}
