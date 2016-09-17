package heartbeat.social.tcs.socialhb.sqliteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import heartbeat.social.tcs.socialhb.bean.SignInUser;
import heartbeat.social.tcs.socialhb.bean.UserProfile;

/**
 * Created by admin on 26/07/16.
 */
public class ProfileDBHelper extends SQLiteOpenHelper
{

    private static final String DB_NAME                = "SQLiteUserProfileDB";
    private static final int DATABASE_VERSION          =  1;
    private static final String TABLE_NAME             = "user_profile_table";
    private static final String KEY_ID                 = "ID";
    private static final String KEY_USER_ID            = "USER_ID";
    private static final String KEY_EMP_ID             = "EMP_ID";
    private static final String KEY_EMP_FIRST_NAME     = "EMP_FIRST_NAME";
    private static final String KEY_EMP_LAST_NAME      = "EMP_LAST_NAME";
    private static final String KEY_EMP_EMAIL          = "EMP_EMAIL";


    public ProfileDBHelper(Context context)
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
                + KEY_EMP_ID +" TEXT,"
                + KEY_EMP_FIRST_NAME + " TEXT,"
                + KEY_EMP_LAST_NAME + " TEXT,"
                + KEY_EMP_EMAIL + " TEXT"
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

    public void addUserProfileData(UserProfile userProfile)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, String.valueOf(userProfile.getUser_id()));
        values.put(KEY_EMP_ID, String.valueOf(userProfile.getEmp_id()));
        values.put(KEY_EMP_FIRST_NAME, String.valueOf(userProfile.getFirst_name()));
        values.put(KEY_EMP_LAST_NAME, String.valueOf(userProfile.getLast_name()));
        values.put(KEY_EMP_EMAIL, String.valueOf(userProfile.getOfficial_email()));
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public String getUserID(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_USER_ID, KEY_EMP_FIRST_NAME, KEY_EMP_LAST_NAME, KEY_EMP_EMAIL}, KEY_ID + "=?",
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

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_USER_ID, KEY_EMP_ID, KEY_EMP_FIRST_NAME, KEY_EMP_LAST_NAME, KEY_EMP_EMAIL}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         =  Integer.parseInt(cursor.getString(0));
        String user_id1 =  cursor.getString(1);
        String emp_id   =  cursor.getString(2);


        db.close();
        return emp_id;
    }

    public String getEmpFirstName(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_USER_ID, KEY_EMP_ID, KEY_EMP_FIRST_NAME, KEY_EMP_LAST_NAME, KEY_EMP_EMAIL}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         =  Integer.parseInt(cursor.getString(0));
        String user_id1 =  cursor.getString(1);
        String emp_id   =  cursor.getString(2);
        String firstname = cursor.getString(3);


        db.close();
        return firstname;
    }

    public String getEmpLastName(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER_ID, KEY_EMP_ID, KEY_EMP_FIRST_NAME, KEY_EMP_LAST_NAME, KEY_EMP_EMAIL}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         =  Integer.parseInt(cursor.getString(0));
        String user_id1 =  cursor.getString(1);
        String emp_id   =  cursor.getString(2);
        String emp_lastname = cursor.getString(4);
        db.close();
        return emp_lastname;
    }


    public String getEmpEmail(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER_ID, KEY_EMP_ID, KEY_EMP_FIRST_NAME, KEY_EMP_LAST_NAME, KEY_EMP_EMAIL}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         =  Integer.parseInt(cursor.getString(0));
        String user_id1 =  cursor.getString(1);
        String emp_id   =  cursor.getString(2);
        String emp_email=  cursor.getString(5);
        db.close();
        return emp_email;
    }

    public void deleteUserProfileTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();

    }
}
