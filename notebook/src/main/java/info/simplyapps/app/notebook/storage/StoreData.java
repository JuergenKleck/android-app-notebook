package info.simplyapps.app.notebook.storage;

import java.util.ArrayList;
import java.util.List;

import info.simplyapps.app.notebook.storage.dto.Entry;

public class StoreData extends info.simplyapps.appengine.storage.StoreData {

    private static final long serialVersionUID = 5696810296031292822L;

    public List<Entry> entries;

    public StoreData() {
        entries = new ArrayList<Entry>();
    }

    public static StoreData getInstance() {
        return (StoreData) info.simplyapps.appengine.storage.StoreData.getInstance();
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
