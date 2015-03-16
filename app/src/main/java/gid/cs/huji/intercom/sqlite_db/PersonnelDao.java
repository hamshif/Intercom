package gid.cs.huji.intercom.sqlite_db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import gid.cs.huji.intercom.model.Personnel;
import gid.cs.huji.intercom.model.Room;
import gid.db_util.CommonKeys;
import gid.interfaces.ITableDao;

/**
 * Created by gideonbar on 12/03/15.
 */

public class PersonnelDao implements ITableDao<Personnel>
{
    private static final String TAG = PersonnelDao.class.getSimpleName();
    private SQLiteDatabase db;

    public PersonnelDao(SQLiteDatabase db)
    {
        this.db = db;
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
                CommonKeys._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CommonKeys.SERVER_ID + " INTEGER, " +
                Personnel.ROOM_ID + " INTEGER, " +
                CommonKeys.NAME + " TEXT NOT NULL, " +
                Personnel.SURNAME + " TEXT NOT NULL, " +
                Personnel.PATH + " TEXT NOT NULL, " +
                CommonKeys.LAST_UPDATE + " INTEGER"
            ;

            String meta =
                    "UNIQUE(" + CommonKeys.SERVER_ID + ") ON CONFLICT REPLACE, " +
                    "FOREIGN KEY(" + Personnel.ROOM_ID + ") REFERENCES " + Room.ROOM + "(" + CommonKeys._ID + ")";

            db.execSQL
            (
                "CREATE TABLE IF NOT EXISTS " + Personnel.PERSONNEL +
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
        contentValues.put(CommonKeys.SERVER_ID, personnel.getServerId());
        contentValues.put(CommonKeys.NAME, personnel.getName());
        contentValues.put(Personnel.SURNAME, personnel.getSurname());
        contentValues.put(Personnel.PATH, personnel.getPath());
        contentValues.put(Personnel.ROOM_ID, personnel.getRoom().getId());
        contentValues.put(CommonKeys.LAST_UPDATE, System.currentTimeMillis());

        db.insert(Personnel.PERSONNEL, null, contentValues);

        Log.i(TAG, "Persisted person in DB");
    }

    @Override
    public Personnel getModelObjects(String[] ids)
    {
        ArrayList<Personnel> p_list = new ArrayList<Personnel>();

        Personnel personnel;
        Room room;

        RoomDao roomDao = new RoomDao(db);

        int _id;
        int server_id;
        int room_id;
        String name;
        String surname;
        String path;
        int i_last_update;

        Cursor cursor = db.rawQuery("SELECT * " + Personnel.PERSONNEL + " WHERE _id = ?", ids);
        cursor.moveToFirst();


//        while (!cursor.isAfterLast())
//        {
//            cursor.moveToNext();
        _id = cursor.getInt(cursor.getColumnIndex(CommonKeys._ID));
        server_id = cursor.getInt(cursor.getColumnIndex(CommonKeys.SERVER_ID));
        room_id = cursor.getInt(cursor.getColumnIndex(Personnel.ROOM_ID));
        name = cursor.getString(cursor.getColumnIndex(CommonKeys.NAME));
        surname = cursor.getString(cursor.getColumnIndex(Personnel.SURNAME));
        path = cursor.getString(cursor.getColumnIndex(Personnel.PATH));
        i_last_update = cursor.getInt(cursor.getColumnIndex(CommonKeys.LAST_UPDATE));

        room = roomDao.getModelObjects(new String[]{"" + room_id});

        personnel = new Personnel(_id, server_id, name, surname, path, room);
//        }

        return personnel;
    }

    public void deleteObject(Personnel personnel)
    {
        // Delete from DB where id match
        db.delete(Room.ROOM, CommonKeys._ID + " = " + personnel.getId(), null);
    }


    public ArrayList<Personnel> getPersonnelList(String[] ids)
    {
        ArrayList<Personnel> personnel_l = new ArrayList<Personnel>();

        Personnel personnel;
        Room room;

        RoomDao roomDao = new RoomDao(db);

        int _id;
        int server_id;
        int room_id;
        String name;
        String surname;
        String path;
        int i_last_update;

        try
        {
            String q = "SELECT * FROM " + Personnel.PERSONNEL;

            Cursor cursor = db.rawQuery(q, ids);
            cursor.moveToFirst();

            while (!cursor.isAfterLast())
            {
                    cursor.moveToNext();
                _id = cursor.getInt(cursor.getColumnIndex(CommonKeys._ID));
                server_id = cursor.getInt(cursor.getColumnIndex(CommonKeys.SERVER_ID));
                room_id = cursor.getInt(cursor.getColumnIndex(Personnel.ROOM_ID));
                name = cursor.getString(cursor.getColumnIndex(CommonKeys.NAME));
                surname = cursor.getString(cursor.getColumnIndex(Personnel.SURNAME));
                path = cursor.getString(cursor.getColumnIndex(Personnel.PATH));
                i_last_update = cursor.getInt(cursor.getColumnIndex(CommonKeys.LAST_UPDATE));

                room = roomDao.getModelObjects(new String[]{"" + room_id});

                personnel = new Personnel(_id, server_id, name, surname, path, room);

                personnel_l.add(personnel);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "", e);
        }


        return personnel_l;
    }
}
