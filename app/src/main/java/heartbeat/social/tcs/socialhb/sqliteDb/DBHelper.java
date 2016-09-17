package heartbeat.social.tcs.socialhb.sqliteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import heartbeat.social.tcs.socialhb.bean.SignInUser;
import heartbeat.social.tcs.socialhb.bean.User;

/**
 * Created by admin on 18/07/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME          = "SQLiteUserDB";
    private static final int DATABASE_VERSION    =  1;
    private static final String TABLE_NAME       = "user_table";
    private static final String KEY_ID           = "ID";
    private static final String KEY_USER_ID      = "USER_ID";
    private static final String KEY_EMP_ID       = "EMP_ID";


    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER_ID + " TEXT,"
                + KEY_EMP_ID +" TEXT "
                + ")";


        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_QUERY  = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_QUERY);

        // Create tables again
        onCreate(db);
    }

    public void addUserData(SignInUser signInUser)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, String.valueOf(signInUser.getUser().getId()));
        values.put(KEY_EMP_ID, String.valueOf(signInUser.getUser().getEmp_id()));
        db.insert(TABLE_NAME, null, values);
        db.close();
    }



    public String getUserID(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_USER_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         = Integer.parseInt(cursor.getString(0));
        String user_id1 =  cursor.getString(1);

        db.close();
        return user_id1;
    }


    public String getEmpID(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER_ID, KEY_EMP_ID}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         =  Integer.parseInt(cursor.getString(0));
        String user_id1 =  cursor.getString(1);
        String emp_id   =  cursor.getString(2);

        db.close();
        return emp_id;
    }

    public void deleteUserTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();

    }

}
