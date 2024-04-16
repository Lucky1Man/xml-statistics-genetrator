package org.example;

import com.fasterxml.jackson.core.JsonPointer;

/**
 * Interface with self-explanatory name.
 * It listens to the values of the field with the specified name.
 */
public interface FieldListener {

    /**
     * @return name of the field it is listening to.
     */
    String getListenedFieldName();

    /**
     * This method is used to determine whether current FieldListener consumes values of field with given name.
     * @param fieldName name of field values of which will be passed to nextField()
     * @param jsonPointer location of this field in tree may be used to tune more precisely to which field to listen
     * @return should return whether current FiledListener is listening to given field
     */
    boolean isListeningForField(String fieldName, JsonPointer jsonPointer);

    /**
     * This method receives values of listened field.
     * @param fieldValue next value of listened field
     */
    void nextFieldValue(String fieldValue);
}
