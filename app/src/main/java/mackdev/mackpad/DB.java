package mackdev.mackpad;
/**
 * Created by Micah on 10/22/2016.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
public final class DB {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DB() {}

    /* Inner class that defines the table contents */
    public static class DBNotes implements BaseColumns {
        public static final String TABLE_NAME = "note";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_ALARM = "alarm";
    }

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBNotes.TABLE_NAME + " (" +
                    DBNotes._ID + " INTEGER PRIMARY KEY," +
                    DBNotes.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP +
                    DBNotes.COLUMN_NAME_DATE + " DATE" + " DEFAULT NULL " +
                    DBNotes.COLUMN_NAME_ALARM + " TINYINT" + " DEFAULT 0" +
                    ")";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBNotes.TABLE_NAME;
}

