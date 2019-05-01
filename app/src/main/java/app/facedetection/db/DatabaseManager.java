package app.facedetection.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import app.facedetection.dao.AttendanceDao;

import java.util.ArrayList;

import static app.facedetection.db.DatabaseHelper.*;


/**
 * Created by Satish on 01/04/2019 4:44 PM.
 */
public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    private Context mContext;
    private SQLiteDatabase db;

    public DatabaseManager(Context context) {
        try {
            mContext = context;
            db = new DatabaseHelper(mContext).getWritableDatabase();
        } catch (SQLiteException E) {
            E.printStackTrace();
        }
    }


    public boolean saveUserName(String... o) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(UserName, o[0]);
            cv.put(Userstatus,o[1]);
            cv.put(Userroll,o[2]);

            return db.insert(USer, null, cv) != -1;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;

        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;

        }
    }


    public ArrayList<AttendanceDao> getAll() {
        ArrayList<AttendanceDao> l = new ArrayList<>();
        try {
            String query = String.format("SELECT  * FROM " + USer);
            Cursor c = db.rawQuery(query, null);

            if (c.getCount() > 0 && c.moveToFirst())
                do
                    l.add(new AttendanceDao(c.getString(0), c.getString(1),c.getString(2)));
                while (c.moveToNext());
            c.close();
            System.out.println("kkkkk" + l.toString());
            return l;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return l;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return l;
        }
    }



//  public void update(String userId){
//
//        String mQuery=("update "+USer+" set "+Userstatus+" = 'P' where medicine_roll = '"+userId+"'");
//        Cursor cursor=db.ra(mQuery);
//
//
//  }

    public void updateAttend(String t_id) {
        try {


            String where = String.format(" %s = '%s' and %s = '%s'", Userstatus, "A",Userroll,t_id);

            ContentValues values = new ContentValues();

            values.put(Userstatus, "P");

            int i = db.update(USer, values, where, null);




            //  return i > 0;
        } catch (SQLiteException E) {
            E.printStackTrace();
            //  return false;
        } catch (IllegalStateException E) {
            E.printStackTrace();
            //  return true;
        }
    }










}
