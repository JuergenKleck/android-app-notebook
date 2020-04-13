package info.simplyapps.app.notebook;

public class Constants {

    public static final String DATABASE = "NoteBook.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CONFIG_AUTOTEXTSIZE = "autotextsize";
    public static final String CONFIG_TEXTSIZE = "textsize";
    public static final String CONFIG_AUTOSORT = "autosort";

    public static final String DEFAULT_CONFIG_AUTOSORT = Boolean.FALSE.toString();
    public static final String DEFAULT_CONFIG_AUTOTEXTSIZE = Boolean.TRUE.toString();
    public static final String DEFAULT_CONFIG_TEXTSIZE = Integer.toString(24);

    public static final String CONFIG_TEXT_UNIT = "sp";
    public static final int CONFIG_TEXT_ADD = 20;

    public static final String DATE_FORMAT = "yyyyddmmhhMM";
}
