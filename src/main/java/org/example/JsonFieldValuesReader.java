package org.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

/**
 * Reads all field values of given source.
 */
public class JsonFieldValuesReader {

    private final List<? extends FieldListener> fieldListeners;

    public JsonFieldValuesReader(List<? extends FieldListener> fieldListeners) {
        this.fieldListeners = fieldListeners;
    }

    /**
     * Goes through all field values, passing them based on whether FieldListener is listening to met field name.
     * It should be supplied with JsonParser. The provided JsonParser is managed by method it-self so should not be used
     * outside.
     * @param jsonParserSupplier supplier is used here to emphasise that returned JsonParser should not be used elsewhere as can cause unpredictable behaviour
     */
    public void goThroughAllData(Supplier<? extends JsonParser> jsonParserSupplier) {
        try (JsonParser jParser = jsonParserSupplier.get()) {
            while (jParser.nextToken() != null) {
                boolean isValueToken = isValueToken(jParser.currentToken());
                if (isValueToken) {
                    for (FieldListener listener : fieldListeners) {
                        if (listener.isListeningForField(
                                jParser.currentName(), jParser.getParsingContext().pathAsPointer()
                        )) {
                            listener.nextFieldValue(jParser.getText());
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValueToken(JsonToken token) {
        return token == JsonToken.VALUE_FALSE ||
                token == JsonToken.VALUE_TRUE ||
                token == JsonToken.VALUE_NULL ||
                token == JsonToken.VALUE_STRING ||
                token == JsonToken.VALUE_NUMBER_FLOAT ||
                token == JsonToken.VALUE_NUMBER_INT;
    }

}
