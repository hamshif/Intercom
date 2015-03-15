package gid.cs.huji.intercom.sqlite_db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import gid.cs.huji.intercom.model.Personnel;
import gid.cs.huji.intercom.model.Room;
import gid.db_util.CommonColumnNames;
import gid.interfaces.ITableDao;

/**
 * Created by gideonbar on 12/03/15.
 */

public class PersonnelDao implements ITableDao<Personnel>
{
    private static final String TAG = PersonnelDao.class.getSimpleName();
    private SQLiteDatabase db;

    public PersonnelDao(IntercomDBHelper dbHelper)
    {
        db = dbHelper.getWritableDatabase();
    }

    public void closeDB()
    {
        db.close();
    }


    public void createTable()
    {
        try
        {
            String columns =
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CommonColumnNames.SERVER_ID + " INTEGER, " +
                "FOREIGN KEY(" + Personnel.ROOM_ID + ") REFERENCES id , " +
                CommonColumnNames.NAME + " TEXT NOT NULL, " +
                Personnel.SURNAME + " TEXT NOT NULL, " +
                Personnel.PATH + " TEXT NOT NULL, "
            ;

            String meta = "";

            db.execSQL
            (
                "CREATE TABLE IF NOT EXISTS" + Personnel.PERSONNEL +
                " ( " +
                    columns + ", " + meta +
                " );"
            );
        }
        catch(Exception e)
        {
            Log.e(TAG, "couldn't create table", e);
        }
    }

    public void dropTable()
    {
        db.execSQL("DROP TABLE IF EXISTS " + Personnel.PERSONNEL);
    }

    public void persistObject(Personnel personnel)
    {
        Log.i(TAG, "trying to insert personnel to db");

        ContentValues contentValues = new ContentValues();
        contentValues.put(CommonColumnNames.SERVER_ID, personnel.getServerId());
        contentValues.put(CommonColumnNames.NAME, personnel.getName());
        contentValues.put(Personnel.SURNAME, personnel.getSurname());
        contentValues.put(Personnel.PATH, personnel.getPath());
        contentValues.put(Personnel.ROOM_ID, personnel.getRoom().getId());

        db.insert(Personnel.PERSONNEL, null, contentValues);

        Log.i(TAG, "Persisted person in DB");
    }

    public void deleteObject(Personnel personnel)
    {
        // Delete from DB where id match
        db.delete(Room.ROOM, "id = " + personnel.getId(), null);
    }

//   public List getPersonnelList()
//   {
//	   Log.i(TAG, "trying to get result from db");
//
//       List resultList = new ArrayList();
//
//       // Name of the columns we want to select
//       String[] tableColumns = new String[] {"id","category"};
//
//       // Query the database
//       Cursor cursor = db.query("results", tableColumns, null, null, null, null, null);
//       cursor.moveToFirst();
//
//       // Iterate the results
//       while (!cursor.isAfterLast())
//       {
//           PersonnelDB result = new PersonnelDB();
//           // Take values from the DB
//           result.setId(cursor.getInt(0));
//           result.setCategory(cursor.getString(1));
//
//           // Add to the DB
//           resultList.add(result);
//
//           // Move to the next result
//           cursor.moveToNext();
//       }
//
//       return resultList;
//   }
}