package org.example;

import com.fasterxml.jackson.core.JsonParser;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * <p>This class is used to run statistics process from A to Z. The process consists of 2 stages.
 * Stages are executed one after another</p>
 * <ul>
 *     <li>First stage is handling each JsonParser in its own thread.</li>
 *     <li>Second stage is providing all gathered statistics to all statisticsConsumers.</li>
 * </ul>
 */
public class JsonStatisticsProcessor implements AutoCloseable {

    private final ExecutorService executorService;
    private final List<? extends StatisticsByFieldService> fieldServices;
    private final List<? extends JsonParser> jsonParsers;
    private final List<BiConsumer<String, List<FieldValueToItsCount>>> statisticsConsumers;

    public JsonStatisticsProcessor(List<? extends StatisticsByFieldService> fieldServices, List<? extends JsonParser> jsonParsers,
                                   List<BiConsumer<String, List<FieldValueToItsCount>>> statisticsConsumers) {
        this(
                Executors.newFixedThreadPool(jsonParsers.size() + statisticsConsumers.size()),
                fieldServices,
                jsonParsers,
                statisticsConsumers
        );
    }

    public JsonStatisticsProcessor(ExecutorService executorService, List<? extends StatisticsByFieldService> fieldServices,
                                   List<? extends JsonParser> jsonParsers,
                                   List<BiConsumer<String, List<FieldValueToItsCount>>> statisticsConsumers) {
        this.executorService = executorService;
        this.fieldServices = fieldServices;
        this.jsonParsers = jsonParsers;
        this.statisticsConsumers = statisticsConsumers;
    }

    /**
     * This method is used to start the formation of statistics.
     * @return returns object representing 2 stages. Also see {@link ReadersFutureAndConsumersFuture}.
     */
    public ReadersFutureAndConsumersFuture run() {
        ReadersFutureAndConsumersFuture future = new ReadersFutureAndConsumersFuture();
        future.setReadersFuture(CompletableFuture.allOf(
                jsonParsers.stream().map(parser ->
                        CompletableFuture.runAsync(
                                () -> new JsonFieldValuesReader(fieldServices).goThroughAllData(() -> parser),
                                executorService
                        )
                ).toArray(CompletableFuture[]::new)
        ).thenRun(() -> future.setConsumersFuture(asyncSupplyConsumersWithNewStatistics())));
        return future;
    }

    private CompletableFuture<Void> asyncSupplyConsumersWithNewStatistics() {
        List<CompletableFuture<Void>> consumersFuture = new LinkedList<>();
        fieldServices.forEach(service ->
                consumersFuture.add(CompletableFuture.runAsync(() ->
                                statisticsConsumers.forEach(consumer -> consumer.accept(
                                        service.getListenedFieldName(), service.getCurrentStatistics()
                                )),
                        executorService
                ))
        );
        return CompletableFuture.allOf(consumersFuture.toArray(CompletableFuture[]::new));
    }

    /**
     * Is used to shut down underlying thread pool
     */
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public void close() {
        shutdown();
    }

    /**
     * This class holds 2 stages described in {@link JsonStatisticsProcessor}.
     */
    @Data
    public static class ReadersFutureAndConsumersFuture {
        /**
         * Represents first stage. Can not be null as completion of this stage is used to start second stage.
         */
        private CompletableFuture<Void> readersFuture;

        /**
         * Represents second stage. Can be null as is assigned after all consumers received data.
         */
        private CompletableFuture<Void> consumersFuture;

        public Optional<CompletableFuture<Void>> getConsumersFuture() {
            return Optional.ofNullable(consumersFuture);
        }
    }
}
