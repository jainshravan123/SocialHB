package heartbeat.social.tcs.socialhb.sqliteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import heartbeat.social.tcs.socialhb.bean.UserFirebaseToken;

/**
 * Created by admin on 29/07/16.
 */
public class FirebaseTokenStorage extends SQLiteOpenHelper {

    private static final String DB_NAME          = "FirebaseTokenDB";
    private static final int DATABASE_VERSION    =  1;
    private static final String TABLE_NAME       = "token_table";
    private static final String KEY_ID           = "ID";
    private static final String KEY_TOKEN_ID     = "TOKEN_ID";
    private static final String KEY_TOKEN        = "TOKEN";


   private static final String TAG = "FirebaseTokenStorage";


    public FirebaseTokenStorage(Context context)
    {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TOKEN    + " TEXT,"
                + KEY_TOKEN_ID + " TEXT"
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

    public void addToken(UserFirebaseToken userFirebaseToken)
    {
        Log.e(TAG, "TOKEN ID : "+userFirebaseToken.getId());
        Log.e(TAG, "TOKEN    :  "+userFirebaseToken.getToken());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TOKEN, String.valueOf(userFirebaseToken.getToken()));
        values.put(KEY_TOKEN_ID, String.valueOf(userFirebaseToken.getId()));
        db.insert(TABLE_NAME, null, values);
        db.close();
    }



    public String getToken(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_TOKEN}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        String token  = cursor.getString(1);

        db.close();
        return token;
    }

    public String getTokenId(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_TOKEN, KEY_TOKEN_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        String token_id  = cursor.getString(2);

        db.close();
        return token_id;
    }



    public boolean checkTokenExistence(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_TOKEN}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

      if(cursor.getCount() == 0){
          return false;
      }



        db.close();
        return true;
    }

    public void deleteTokenTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();

    }

}
