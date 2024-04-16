package org.example;

import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

/**
 * Writes statistics in xml format.
 */
public class XmlStatisticsWriter {

    /**
     * Writes statistics in xml format using given generator. Generator is managed by this method and should not be used outside.
     * @param generatorSupplier supplier is used here to emphasise that it should not be used outside as its behaviour outside is unpredictable
     * @param statistics statistics to be written in xml
     */
    public void writeToFile(Supplier<ToXmlGenerator> generatorSupplier, List<FieldValueToItsCount> statistics) {
        try(ToXmlGenerator generator = generatorSupplier.get()){
            generator.setPrettyPrinter(new DefaultXmlPrettyPrinter());
            generator.setNextName(new QName("statistics"));
            generator.writeStartObject();
            statistics.forEach(stat -> writeItem(generator, stat));
            generator.writeEndObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeItem(ToXmlGenerator generator, FieldValueToItsCount item) {
        try {
            generator.setNextIsUnwrapped(false);
            generator.writeFieldName("item");
            generator.writeStartObject();
            generator.writeFieldName("value");
            generator.writeString(item.getFieldValue());
            generator.writeFieldName("count");
            generator.writeNumber(item.getNumberOfOccurrences());
            generator.writeEndObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
