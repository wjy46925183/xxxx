package gongren.com.dlg.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2017/5/13.
 */

public class MySqliteOpenHelper extends SQLiteOpenHelper {
    public SQLiteDatabase db;
    public MySqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public MySqliteOpenHelper(Context context){
        this(context,"dlg.db",null,1);
        db = getReadableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table search(id int,searchName varchar(20)," +
                "searchDate varchar(20),postType varchar(10))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
