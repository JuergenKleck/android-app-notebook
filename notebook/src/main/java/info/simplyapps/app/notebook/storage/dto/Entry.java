package info.simplyapps.app.notebook.storage.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import info.simplyapps.appengine.storage.dto.BasicTable;

public class Entry extends BasicTable implements Serializable {

    /**
     * serial id
     */
    private static final long serialVersionUID = -213755492728977917L;

    public String text;
    public Date date;
    public boolean expand;

    public Entry() {
        date = Calendar.getInstance().getTime();
    }

}
