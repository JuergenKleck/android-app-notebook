package com.juergenkleck.android.app.notebook;

import android.content.Context;

import java.util.List;

import com.juergenkleck.android.app.notebook.storage.StoreData;
import com.juergenkleck.android.app.notebook.storage.dto.Entry;
import com.juergenkleck.android.appengine.storage.dto.Configuration;

/**
 * Android app - Notebook
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class SystemHelper extends com.juergenkleck.android.appengine.SystemHelper {

    public synchronized static List<Entry> getEntries() {
        return StoreData.getInstance().entries;
    }

    public synchronized static void addEntry(Entry i) {
        if (StoreData.getInstance() != null && StoreData.getInstance().entries != null) {
            StoreData.getInstance().entries.add(i);
        }
    }

    public synchronized static Entry getEntry(long index) {
        if (StoreData.getInstance() != null && StoreData.getInstance().entries != null) {
            for (Entry obj : StoreData.getInstance().entries) {
                if (obj.id == index) {
                    return obj;
                }
            }
        }
        return null;
    }


    public static boolean notEmpty(String s) {
        return s != null && s.length() > 0;
    }

    public static int getTextSize(Context context) {
        Configuration config = SystemHelper.getConfiguration(Constants.CONFIG_TEXTSIZE, Constants.DEFAULT_CONFIG_TEXTSIZE);
        return Integer.parseInt(config.value);
    }

    public static boolean isAutoTextSize(Context context) {
        Configuration config = SystemHelper.getConfiguration(Constants.CONFIG_AUTOTEXTSIZE, Constants.DEFAULT_CONFIG_AUTOTEXTSIZE);
        return Boolean.parseBoolean(config.value);
    }

}
