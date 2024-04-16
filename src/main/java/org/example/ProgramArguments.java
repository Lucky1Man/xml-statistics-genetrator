package org.example;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This class is used to parce given program arguments.
 */
public class ProgramArguments {
    private static final int ARGUMENT_NAME = 0;
    private static final int ARGUMENT_VALUE = 1;
    private static final String JSON_DIR_ARG = "--JSON_DIR";
    private static final String STATISTICS_BY_FIELDS_ARG = "--STATISTICS_BY_FIELDS";

    private final List<String> jsonFilePaths = new LinkedList<>();
    private final Set<String> watchedFields = new HashSet<>();

    public ProgramArguments(String[] programArgs) {
        for (String arg : programArgs) {
            String[] argument = arg.split("=");
            if (argument.length < 2) {
                throw new IllegalArgumentException("Illegal argument passed: " + arg);
            }
            handleJsonDirArgument(argument);
            handleWatchedFieldsArgument(argument);
        }
    }

    private void handleJsonDirArgument(String[] validArgument) {
        if (!JSON_DIR_ARG.equals(validArgument[ARGUMENT_NAME])) {
            return;
        }
        try {
            String jsonDirPath = validArgument[ARGUMENT_VALUE];
            URL resource = this.getClass().getClassLoader().getResource(jsonDirPath);
            File jsonDir;
            if(resource != null) {
                jsonDir = new File(resource.toURI());
            } else
                jsonDir = FileUtils.getFile(jsonDirPath);
            if (jsonDir.isFile()) {
                throw new IllegalArgumentException("Given path is pointing to a file: " + jsonDirPath);
            }
            for (File innerFile : FileUtils.listFiles(jsonDir, null, false)) {
                if (innerFile.isFile()) {
                    if (Arrays.asList(innerFile.getName().split("\\.")).contains("json")) {
                        jsonFilePaths.add(innerFile.getAbsolutePath());
                    } else {
                        System.out.printf("Detected non-json file %s, %n", innerFile.getAbsolutePath());
                    }
                }
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleWatchedFieldsArgument(String[] validArgument) {
        if (!STATISTICS_BY_FIELDS_ARG.equals(validArgument[ARGUMENT_NAME])) {
            return;
        }
        watchedFields.addAll(Arrays.asList(validArgument[ARGUMENT_VALUE].split(",")));
    }

    /**
     * @return paths for all found json files inside first layer of given directory.
     */
    public List<String> getJsonFilePaths() {
        return Collections.unmodifiableList(jsonFilePaths);
    }

    /**
     * @return all field names that should be watched
     */
    public Set<String> getWatchedFields() {
        return Collections.unmodifiableSet(watchedFields);
    }

}
