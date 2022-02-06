package com.juergenkleck.android.app.notebook.storage;

import android.provider.BaseColumns;

/**
 * Android app - Notebook
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public final class StorageContract extends com.juergenkleck.android.appengine.storage.StorageContract {

    // Prevents the StorageContract class from being instantiated.
    private StorageContract() {
    }

    public static abstract class TableEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_DATE = "date";
    }

}
