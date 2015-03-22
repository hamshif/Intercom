package gid.cs.huji.intercom.sqlite_db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;

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
//                "UNIQUE(" + Room.BUILDING + ", " + Room.WING + ", " + Room.FLOOR + ", " + Room.NUM + ")";

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
        db.execSQL("DROP TABLE IF EXISTS " + Room.ROOM + ";");
    }

    public Room persistObject(Room room)
    {
        Log.i(TAG, "trying to insert room to db");

        ContentValues contentValues = new ContentValues();
        contentValues.put(CommonKeys.SERVER_ID, room.getServer_id());
        contentValues.put(Room.BUILDING, room.getBuilding());
        contentValues.put(Room.WING, room.getWing());
        contentValues.put(Room.FLOOR, room.getFloor());
        contentValues.put(Room.NUM, room.getNum());
        contentValues.put(CommonKeys.LAST_UPDATE, System.currentTimeMillis());

        long a = db.insert(Room.ROOM, null, contentValues);

        Log.d(TAG, room.getBrowseText() + " id: " + a);

        room.setId(a);

        Log.i(TAG, "Persisted room in DB");

        return room;
    }

    @Override
    public Room getModelObjects(String[] ids)
    {
        Room room = null;

        int _id;
        int server_id;
        String building;
        String wing;
        int floor;
        int num;
        int i_last_update;

        String q = "SELECT * FROM " + Room.ROOM + " WHERE " + CommonKeys._ID + " = " + ids[0] + ";";
        Log.d(TAG, "query: " + q);
        Cursor cursor = db.rawQuery(q, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            for(String s: cursor.getColumnNames())
            {
                Log.i(TAG, "room table column: " + s);
            }

            _id = cursor.getInt(cursor.getColumnIndex(CommonKeys._ID));
            server_id = cursor.getInt(cursor.getColumnIndex(CommonKeys.SERVER_ID));
            building = cursor.getString(cursor.getColumnIndex(Room.BUILDING));
            wing = cursor.getString(cursor.getColumnIndex(Room.WING));
            floor = cursor.getInt(cursor.getColumnIndex(Room.FLOOR));
            num = cursor.getInt(cursor.getColumnIndex(Room.NUM));
            i_last_update = cursor.getInt(cursor.getColumnIndex(CommonKeys.LAST_UPDATE));

            room = new Room(_id, server_id, building, wing, floor, num);


            //TODO put this in a list only the last value is returned

            cursor.moveToNext();
        }

        return room;
    }


    public HashMap<Integer, Room> getIDHash(String[] ids)
    {
        HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
        Room room = null;

        int _id;
        int server_id;
        String building;
        String wing;
        int floor;
        int num;
        int i_last_update;

        String q = "SELECT * FROM " + Room.ROOM;

        if(ids != null && ids.length > 0)
        {
            q +=  " WHERE " + CommonKeys._ID + "=?";
        }


        Log.d(TAG, "query: " + q);
        Cursor cursor = db.rawQuery(q, ids);

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
//            for(String s: cursor.getColumnNames())
//            {
//                Log.i(TAG, "room table column: " + s);
//            }

            _id = cursor.getInt(cursor.getColumnIndex(CommonKeys._ID));
            server_id = cursor.getInt(cursor.getColumnIndex(CommonKeys.SERVER_ID));
            building = cursor.getString(cursor.getColumnIndex(Room.BUILDING));
            wing = cursor.getString(cursor.getColumnIndex(Room.WING));
            floor = cursor.getInt(cursor.getColumnIndex(Room.FLOOR));
            num = cursor.getInt(cursor.getColumnIndex(Room.NUM));
            i_last_update = cursor.getInt(cursor.getColumnIndex(CommonKeys.LAST_UPDATE));


            room = new Room(_id, server_id, building, wing, floor, num);

            Log.d(TAG, "id: " + _id + " Room: " + room.getBrowseText());

            rooms.put(_id, room);

            //TODO put this in a list only the last value is returned

            cursor.moveToNext();
        }

        return rooms;
    }



    public void deleteObject(Room room)
    {
        // Delete from DB where id match
        db.delete(Room.ROOM, CommonKeys._ID + " = " + room.getId(), null);
    }

}
