package com.juergenkleck.android.app.notebook.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.juergenkleck.android.app.notebook.Constants;
import com.juergenkleck.android.app.notebook.storage.dto.Entry;
import com.juergenkleck.android.appengine.storage.dto.BasicTable;

/**
 * Android app - Notebook
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class DBDriver extends com.juergenkleck.android.appengine.storage.DBDriver {


    private static final String SQL_CREATE_ENTRY =
            "CREATE TABLE " + StorageContract.TableEntry.TABLE_NAME + " (" +
                    StorageContract.TableEntry._ID + " INTEGER PRIMARY KEY," +
                    StorageContract.TableEntry.COLUMN_DATE + TYPE_TEXT + COMMA_SEP +
                    StorageContract.TableEntry.COLUMN_TEXT + TYPE_TEXT +
                    " );";

    private static final String SQL_DELETE_ENTRY =
            "DROP TABLE IF EXISTS " + StorageContract.TableEntry.TABLE_NAME;

    public DBDriver(String dataBaseName, int dataBaseVersion, Context context) {
        super(dataBaseName, dataBaseVersion, context);
    }

    public static DBDriver getInstance() {
        return (DBDriver) com.juergenkleck.android.appengine.storage.DBDriver.getInstance();
    }

    @Override
    public void createTables(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRY);
    }

    @Override
    public void upgradeTables(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public String getExtendedTable(BasicTable data) {
        return Entry.class.isInstance(data) ? StorageContract.TableEntry.TABLE_NAME : null;
    }

    @Override
    public void storeExtended(com.juergenkleck.android.appengine.storage.StoreData data) {
        store(StoreData.class.cast(data).entries);
    }

    @Override
    public void readExtended(com.juergenkleck.android.appengine.storage.StoreData data, SQLiteDatabase db) {
        readEntries(StoreData.class.cast(data), db);
    }

    @Override
    public com.juergenkleck.android.appengine.storage.StoreData createStoreData() {
        return new StoreData();
    }

    public void clear(List<Entry> dataList) {
        for (Entry data : dataList) {
            delete(data);
        }
    }

    public boolean store(List<Entry> dataList) {
        boolean stored = true;
        for (Entry data : dataList) {
            stored &= store(data);
        }
        return stored;
    }

    public boolean store(Entry data) {
        ContentValues values = new ContentValues();
        values.put(StorageContract.TableEntry.COLUMN_TEXT, data.text);
        values.put(StorageContract.TableEntry.COLUMN_DATE, new SimpleDateFormat(Constants.DATE_FORMAT).format(data.date));
        return persist(data, values, StorageContract.TableEntry.TABLE_NAME);
    }

    private void readEntries(StoreData data, SQLiteDatabase db) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                StorageContract.TableEntry._ID,
                StorageContract.TableEntry.COLUMN_DATE,
                StorageContract.TableEntry.COLUMN_TEXT
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = StorageContract.TableEntry.COLUMN_DATE + " ASC";
        String selection = null;
        String[] selectionArgs = null;
        Cursor c = db.query(
                StorageContract.TableEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasResults = c.moveToFirst();
        while (hasResults) {
            Entry i = new Entry();
            i.id = c.getLong(c.getColumnIndexOrThrow(StorageContract.TableEntry._ID));
            i.text = c.getString(c.getColumnIndexOrThrow(StorageContract.TableEntry.COLUMN_TEXT));
            try {
                i.date = new SimpleDateFormat(Constants.DATE_FORMAT).parse(c.getString(c.getColumnIndexOrThrow(StorageContract.TableEntry.COLUMN_DATE)));
            } catch (IllegalArgumentException | ParseException e) {
                i.date = Calendar.getInstance().getTime();
            }
            data.entries.add(i);
            hasResults = c.moveToNext();
        }
        c.close();
    }

}
