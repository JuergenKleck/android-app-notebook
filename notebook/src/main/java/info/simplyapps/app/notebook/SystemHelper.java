package info.simplyapps.app.notebook;

import android.content.Context;

import java.util.List;

import info.simplyapps.app.notebook.storage.StoreData;
import info.simplyapps.app.notebook.storage.dto.Entry;
import info.simplyapps.appengine.storage.dto.Configuration;

public class SystemHelper extends info.simplyapps.appengine.SystemHelper {

    public synchronized static final List<Entry> getEntries() {
        return StoreData.getInstance().entries;
    }

    public synchronized static final void addEntry(Entry i) {
        if (StoreData.getInstance() != null && StoreData.getInstance().entries != null) {
            StoreData.getInstance().entries.add(i);
        }
    }

    public synchronized static final Entry getEntry(long index) {
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
        return Integer.valueOf(config.value);
    }

    public static boolean isAutoTextSize(Context context) {
        Configuration config = SystemHelper.getConfiguration(Constants.CONFIG_AUTOTEXTSIZE, Constants.DEFAULT_CONFIG_AUTOTEXTSIZE);
        return Boolean.valueOf(config.value);
    }

}
