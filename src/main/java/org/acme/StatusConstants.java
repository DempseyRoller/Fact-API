package org.acme;

public class StatusConstants {

    static final String INVALID_OBJECTID = "{\"message\":\"Wrong id format.\",\"details\":\"The id must consist of only letters from A-Z (case insensitive) and numbers, and its length must be exactly 24.\"}";
    static final String NO_SUCH_ID = "{\"message\":\"No such id in the collection.\"}";
    static final String ALREADY_IN_DATABASE = "{\"message\":\"Id already in database.\"}";
    static final String CONFIRM_DELETEALL = "{\"message\":\"Confirm deletion of all instances by setting query parameter deleteAll to TRUE.\"}";
    static final String MISSING_FIELDS = "{\"message\":\"Missing JSONfields: text and type fields must be set.\"}";
    static final String MISSING_JSON = "{\"message\":\"POST data missing JSON.\"}";

    private StatusConstants() {
    }
}
