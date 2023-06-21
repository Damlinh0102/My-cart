package hanu.a2_2001040116.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String dbname = "mycart";
    private static final int dbversion = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, dbname, null, dbversion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createQuery = "CREATE TABLE items " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "category VARCHAR(50)," +
                "thumbnail TEXT," +
                "unitPrice LONG," +
                "quantity INTEGER)";
        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
