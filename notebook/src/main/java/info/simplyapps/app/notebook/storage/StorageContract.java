package info.simplyapps.app.notebook.storage;

import android.provider.BaseColumns;

public final class StorageContract extends info.simplyapps.appengine.storage.StorageContract {

    // Prevents the StorageContract class from being instantiated.
    private StorageContract() {
    }

    public static abstract class TableEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_DATE = "date";
    }

}
