package org.example;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.example.JsonStatisticsProcessor.ReadersFutureAndConsumersFuture;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public class App {

    public static void main(String[] args) {
        ProgramArguments arguments = new ProgramArguments(args);
        try (JsonStatisticsProcessor jsonStatisticsProcessor = new JsonStatisticsProcessor(
//                Executors.newFixedThreadPool(1),
                getRequiredServices(arguments.getWatchedFields()),
                getAllParsers(arguments.getJsonFilePaths()),
                getStatsConsumers()
        )) {
            ReadersFutureAndConsumersFuture futures = jsonStatisticsProcessor.run();
            futures.getReadersFuture().get();
            Optional<CompletableFuture<Void>> consumersFuture = futures.getConsumersFuture();
            if (consumersFuture.isPresent()) {
                consumersFuture.get().get();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<StatisticsByFieldService> getRequiredServices(Set<String> watchedFields) {
        List<StatisticsByFieldService> requiredServices = new LinkedList<>();
        if (watchedFields.contains("executorId")) {
            requiredServices.add(new StatisticsByExecutorIdService());
        }
        if (watchedFields.contains("description")) {
            requiredServices.add(new StatisticsByDescriptionService());
        }
        if (watchedFields.contains("startTime")) {
            requiredServices.add(new StatisticsByStartTimeService());
        }
        if (watchedFields.contains("finishTime")) {
            requiredServices.add(new StatisticsByFinishTimeService());
        }
        if (watchedFields.contains("guarantorEmails")) {
            requiredServices.add(new StatisticsByGuarantorEmails());
        }
        return requiredServices;
    }

    private static List<JsonParser> getAllParsers(List<String> pathToJsonFiles) {
        JsonFactory jsonFactory = new JsonFactory();
        return pathToJsonFiles.stream().map(path -> getParserForPath(jsonFactory, path)).toList();
    }

    private static JsonParser getParserForPath(JsonFactory jsonFactory, String path) {
        try {
            return jsonFactory.createParser(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<BiConsumer<String, List<FieldValueToItsCount>>> getStatsConsumers() {
        return List.of(
                (fieldName, stats) -> {
                    new XmlStatisticsWriter().writeToFile(
                            () -> getXmlGenerator(fieldName),
                            stats
                    );
                }
        );
    }

    private static ToXmlGenerator getXmlGenerator(String fieldName) {
        try {
            XmlFactory xmlFactory = new XmlFactory();
            return xmlFactory.createGenerator(
                    new File(getAbsolutePath(fieldName)),
                    JsonEncoding.UTF8
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getAbsolutePath(String fieldName) {
        return new File("statistics_by_%s.xml".formatted(fieldName)).getAbsolutePath();
    }

}
