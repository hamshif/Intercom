package gid.cs.huji.intercom.sqlite_db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gid.cs.huji.intercom.model.Personnel;
import gid.cs.huji.intercom.model.Room;
import gid.db_util.CommonKeys;
import gid.interfaces.ITableDao;

/**
 * Created by gideonbar on 12/03/15.
 */

public class RoomDao implements ITableDao<Room>
{
    private static final String TAG = RoomDao.class.getSimpleName();
    private SQLiteDatabase db;

    public RoomDao(SQLiteDatabase db)
    {
        this.db = db;
    }

    public void closeDB()
    {
        db.close();
    }


    public void
    createTable()
    {
        try
        {
            String columns =
                CommonKeys._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CommonKeys.SERVER_ID + " INTEGER, " +
                Room.BUILDING + " TEXT NOT NULL, " +
                Room.WING + " TEXT NOT NULL, " +
                Room.FLOOR + " INTEGER NOT NULL, " +
                Room.NUM + " INTEGER NOT NULL, " +
                CommonKeys.LAST_UPDATE + " INTEGER"
            ;

            String meta =
                "UNIQUE(" + Room.BUILDING + ", " + Room.WING + ", " + Room.FLOOR + ", " + Room.NUM + ") ON CONFLICT REPLACE ";

            db.execSQL
            (
                "CREATE TABLE IF NOT EXISTS " + Room.ROOM +
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
        db.execSQL("DROP TABLE IF EXISTS " + Room.ROOM);
    }

    public void persistObject(Room room)
    {
        Log.i(TAG, "trying to insert room to db");

        ContentValues contentValues = new ContentValues();
        contentValues.put(CommonKeys.SERVER_ID, room.getServer_id());
        contentValues.put(Room.BUILDING, room.getBuilding());
        contentValues.put(Room.WING, room.getWing());
        contentValues.put(Room.FLOOR, room.getFloor());
        contentValues.put(Room.NUM, room.getNum());
        contentValues.put(CommonKeys.LAST_UPDATE, System.currentTimeMillis());

        db.insert(Room.ROOM, null, contentValues);

        Log.i(TAG, "Persisted room in DB");
    }

    @Override
    public Room getModelObjects(String[] ids)
    {
        Room room;

        int _id;
        int server_id;
        String building;
        String wing;
        int floor;
        int num;
        int i_last_update;

        Cursor cursor = db.query(
                Room.ROOM,
                ids,
                null, null, null, null, null
        );

        cursor.moveToFirst();


//        while (!cursor.isAfterLast())
//        {
//            cursor.moveToNext();
            _id = cursor.getInt(cursor.getColumnIndex(CommonKeys._ID));
            server_id = cursor.getInt(cursor.getColumnIndex(CommonKeys.SERVER_ID));
            building = cursor.getString(cursor.getColumnIndex(Room.BUILDING));
            wing = cursor.getString(cursor.getColumnIndex(Room.WING));
            floor = cursor.getInt(cursor.getColumnIndex(Room.FLOOR));
            num = cursor.getInt(cursor.getColumnIndex(Room.NUM));
            i_last_update = cursor.getInt(cursor.getColumnIndex(CommonKeys.LAST_UPDATE));

            room = new Room(_id, server_id, building, wing, floor, num);
//        }

        return room;
    }




    public void deleteObject(Room room)
    {
        // Delete from DB where id match
        db.delete(Room.ROOM, CommonKeys._ID + " = " + room.getId(), null);
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
