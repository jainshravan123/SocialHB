package heartbeat.social.tcs.socialhb.sqliteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import heartbeat.social.tcs.socialhb.bean.QuizScore;
import heartbeat.social.tcs.socialhb.bean.SignInUser;

/**
 * Created by admin on 05/09/16.
 */
public class QuizDBHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME          = "QuizDB";
    private static final int DATABASE_VERSION    =  1;
    private static final String TABLE_NAME       = "quiz_table";
    private static final String KEY_ID           = "ID";
    private static final String KEY_QUIZ_ID      = "QUIZ_ID";
    private static final String KEY_NO_OF_QUS    = "NO_OF_QUS";


    public QuizDBHelper(Context context)
    {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME
                                                            + "("
                                                            + KEY_ID + " INTEGER PRIMARY KEY,"
                                                            + KEY_QUIZ_ID + " TEXT,"
                                                            + KEY_NO_OF_QUS +" TEXT "
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



    public void addQuizStartingData(QuizScore quizScore)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUIZ_ID, String.valueOf(quizScore.getQuiz_id()));
        values.put(KEY_NO_OF_QUS, String.valueOf(quizScore.getNo_of_qus()));
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public QuizScore getQuizStartingData()
    {
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_QUIZ_ID, KEY_NO_OF_QUS}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1            =  Integer.parseInt(cursor.getString(0));
        String quiz_id     =  cursor.getString(1);
        String no_of_qus   =  cursor.getString(2);

        QuizScore quizScore  = new QuizScore();
        quizScore.setQuiz_id(quiz_id);
        quizScore.setNo_of_qus(Integer.parseInt(no_of_qus));
        db.close();

        return quizScore;
    }

    public int getQuizStartDataCountTrial()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        int i=0;
        if (cursor!= null && cursor.moveToFirst())
        {
            do {
                i++;
            }while(cursor.moveToNext());
        }
        return i;
    }


    public boolean checkQuizStartingDataExistence()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        int i=0;
        if (cursor!= null && cursor.moveToFirst())
        {
            do {
            i++;
            }while(cursor.moveToNext());
        }

        if(i == 0)
        {
            return false;
        }

        return true;
    }


    public void deleteQuizTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();

    }


}
