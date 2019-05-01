package app.facedetection.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Satish on 01/04/2019 4:43 PM.
 */
class DatabaseHelper extends SQLiteOpenHelper {
    private static final int iDatabaseVersion = 1;


    private Context mCtx;
    /* ATTENDANCE TABLE */



    static final String USer = "user_table";
    static final String UserName = "sno_dose";
    static final String Userstatus = "medicine_name";
    static final String Userroll = "medicine_roll";




    private String xStudentQuery = String.format(" Create Table %s ( %s Text ,%s TEXT,%s TEXT) ", USer,
            UserName, Userstatus, Userroll);


    DatabaseHelper(Context context) {
        super(context, "ta", null, iDatabaseVersion);
        //   super(context, "ta", nulliDatabaseVersion 1);
        this.mCtx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(xStudentQuery);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {




    }
}


