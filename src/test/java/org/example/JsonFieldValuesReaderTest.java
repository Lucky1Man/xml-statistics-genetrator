package org.example;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JsonFieldValuesReaderTest {

    static final ClassLoader CLASS_LOADER = JsonFieldValuesReaderTest.class.getClassLoader();
    static final Supplier<JsonParser> JSON_PARSER_EDGE_CASES_SUPPLIER = () -> {
        try {
            return new JsonFactory().createParser(CLASS_LOADER.getResource("json-values-edge-cases.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    JsonFieldValuesReader jsonFieldValuesReader;
    @Mock
    FieldListener fieldListener;
    @Captor
    ArgumentCaptor<String> fieldValueCaptor;
    @BeforeEach
    void init() {
        jsonFieldValuesReader = new JsonFieldValuesReader(List.of(fieldListener));
    }

    @Test
    void goThroughAllData_shouldGoThroughAllFieldValues() {
        //given
        List<String> expectedFieldValues = List.of(
                "null",
                "true",
                "42",
                "3.14",
                "Hello, world!",
                "",
                "1",
                "2",
                "3",
                "1",
                "2",
                "3",
                "4",
                "value1",
                "value2",
                "value3",
                "value4",
                "value5",
                "value6",
                "Unicode characters: \u00E9 \u20AC \uD83D\uDE00",
                "Escaped characters: \\n \\t \\/ \\\" \\\\",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                "9223372036854775807",
                "1.7976931348623157e+308",
                "-42",
                "-3.14",
                "2.998e8",
                "value7",
                "Infinity",
                "-Infinity",
                "NaN",
                "0",
                "-0",
                "1e1000"
        );
        given(fieldListener.isListeningForField(any(), any())).willReturn(true);

        //when
        jsonFieldValuesReader.goThroughAllData(JSON_PARSER_EDGE_CASES_SUPPLIER);
        //then
        verify(fieldListener, times(expectedFieldValues.size())).nextFieldValue(fieldValueCaptor.capture());
        assertTrue(fieldValueCaptor.getAllValues().containsAll(expectedFieldValues), "Not all values where read");
    }

    @Test
    void goThroughAllData_shouldPassCorrectJsonPointerToEachFieldName() {
        //given
        class FieldNameToItsPointer {
            String fieldName;
            JsonPointer pointer;

            public FieldNameToItsPointer(String fieldName, String path) {
                this.fieldName = fieldName;
                this.pointer = JsonPointer.compile(path);
            }

        }
        List<FieldNameToItsPointer> fieldNamesToItsPointers = new LinkedList<>();

        BiConsumer<String, String> addFieldToList =
                (fieldName, path) -> fieldNamesToItsPointers.add(new FieldNameToItsPointer(fieldName, path));

        addFieldToList.accept("nullValue", "/nullValue");
        addFieldToList.accept("booleanValue", "/booleanValue");
        addFieldToList.accept("integerValue", "/integerValue");
        addFieldToList.accept("floatValue", "/floatValue");
        addFieldToList.accept("stringValue", "/stringValue");
        addFieldToList.accept("emptyString", "/emptyString");
        addFieldToList.accept(null, "/arrayValue/0");
        addFieldToList.accept(null, "/arrayValue/1");
        addFieldToList.accept(null, "/arrayValue/2");
        addFieldToList.accept(null, "/nestedArray/0/0");
        addFieldToList.accept(null, "/nestedArray/0/1");
        addFieldToList.accept(null, "/nestedArray/1/0");
        addFieldToList.accept(null, "/nestedArray/1/1");
        addFieldToList.accept("key", "/objectValue/key");
        addFieldToList.accept("key", "/nestedObject/nested/key");
        addFieldToList.accept("key", "/objectsInArray/0/key");
        addFieldToList.accept("key", "/objectsInArray/1/nestedArrayWithMixed/0/key");
        addFieldToList.accept(null, "/objectsInArray/1/nestedArrayWithMixed/1");
        addFieldToList.accept(null, "/objectsInArray/1/nestedArrayWithMixed/2");
        addFieldToList.accept("unicodeString", "/unicodeString");
        addFieldToList.accept("escapedString", "/escapedString");
        addFieldToList.accept("longString", "/longString");
        addFieldToList.accept("bigInteger", "/bigInteger");
        addFieldToList.accept("bigFloat", "/bigFloat");
        addFieldToList.accept("negativeInteger", "/negativeInteger");
        addFieldToList.accept("negativeFloat", "/negativeFloat");
        addFieldToList.accept("scientificNotation", "/scientificNotation");
        addFieldToList.accept("", "/");
        addFieldToList.accept("infinityValue", "/infinityValue");
        addFieldToList.accept("negativeInfinityValue", "/negativeInfinityValue");
        addFieldToList.accept("nanValue", "/nanValue");
        addFieldToList.accept("zeroValue", "/zeroValue");
        addFieldToList.accept("negativeZeroValue", "/negativeZeroValue");
        addFieldToList.accept("largeExponent", "/largeExponent");
        //when
        jsonFieldValuesReader.goThroughAllData(JSON_PARSER_EDGE_CASES_SUPPLIER);
        //then
        for (FieldNameToItsPointer fieldNamesToItsPointer : fieldNamesToItsPointers) {
            verify(fieldListener).isListeningForField(fieldNamesToItsPointer.fieldName, fieldNamesToItsPointer.pointer);
        }
    }

    @Test
    @SneakyThrows
    void goThroughAllData_shouldRethrowWrappedIOExceptions_ifJsonParserThrewAny() {
        //given
        String exMessage = "any message";
        JsonParser mockParser = mock(JsonParser.class);
        given(mockParser.nextToken()).willThrow(new IOException(exMessage));
        //then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> jsonFieldValuesReader.goThroughAllData(() -> mockParser));
        assertTrue(thrown.getMessage().contains(exMessage), "Thrown exception does not contain given message");
    }
}
