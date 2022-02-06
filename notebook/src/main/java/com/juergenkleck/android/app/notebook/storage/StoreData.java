package com.juergenkleck.android.app.notebook.storage;

import java.util.ArrayList;
import java.util.List;

import com.juergenkleck.android.app.notebook.storage.dto.Entry;

/**
 * Android app - Notebook
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class StoreData extends com.juergenkleck.android.appengine.storage.StoreData {

    private static final long serialVersionUID = 5696810296031292822L;

    public List<Entry> entries;

    public StoreData() {
        entries = new ArrayList<Entry>();
    }

    public static StoreData getInstance() {
        return (StoreData) com.juergenkleck.android.appengine.storage.StoreData.getInstance();
    }

    /**
     * Update to the latest release
     */
    public boolean update() {
        boolean persist = false;

        // Release 1 - 1.0.0
        if (migration < 1) {
            persist = true;
        }

        migration = 1;
        return persist;
    }

}
