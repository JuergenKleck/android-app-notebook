package com.juergenkleck.android.app.notebook.storage.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Android app - Notebook
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class RootItem {

    public List<Entry> entries;

    public RootItem() {
        entries = new ArrayList<Entry>();
    }

}
