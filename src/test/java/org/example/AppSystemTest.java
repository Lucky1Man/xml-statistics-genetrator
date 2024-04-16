package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppSystemTest {

    @Test
    void main_shouldWireEverythingRightAndProduceRightStatistics_forGivenArguments() {
        //given
        String[] args = new String[2];
        args[0] = "--JSON_DIR=./system";
        args[1] = "--STATISTICS_BY_FIELDS=executorId,guarantorEmails";
        //when
        App.main(args);
        //then
        assertExpectedAgainstResult(
                "./system/expected/statistics_by_executorId.xml",
                "./statistics_by_executorId.xml"
        );
        assertExpectedAgainstResult(
                "./system/expected/statistics_by_guarantorEmails.xml",
                "./statistics_by_guarantorEmails.xml"
        );
    }

    @SneakyThrows
    private void assertExpectedAgainstResult(String pathToExpected, String pathToResult) {
        XmlMapper xmlMapper = new XmlMapper();
        ClassLoader classLoader = xmlMapper.getClass().getClassLoader();
        JsonNode expected = xmlMapper.readTree(new File(classLoader.getResource(pathToExpected).toURI())).iterator().next();
        File resultFile = new File(pathToResult);
        JsonNode result = xmlMapper.readTree(resultFile).iterator().next();;
        assertEquals(expected.size(), result.size(), pathToResult + " does not have same amount of elements");
        assertTrue(checkInclusion(expected, result), pathToResult + " should contain all expected elements");
        Files.delete(Path.of(resultFile.toURI()));
    }

    private static boolean checkInclusion(JsonNode expected, JsonNode result) {
        Set<String> elementsInResult = new HashSet<>();
        for (JsonNode child2 : result) {
            elementsInResult.add(child2.toString());
        }
        for (JsonNode child1 : expected) {
            if (!elementsInResult.contains(child1.toString())) {
                return false;
            }
        }
        return true;
    }
}
