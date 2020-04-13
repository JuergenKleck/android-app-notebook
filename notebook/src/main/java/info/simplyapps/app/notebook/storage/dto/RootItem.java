package info.simplyapps.app.notebook.storage.dto;

import java.util.ArrayList;
import java.util.List;

public class RootItem {

    public List<Entry> entries;

    public RootItem() {
        entries = new ArrayList<Entry>();
    }

}
