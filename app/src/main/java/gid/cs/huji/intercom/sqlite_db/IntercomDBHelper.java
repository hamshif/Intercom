package gid.cs.huji.intercom.sqlite_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IntercomDBHelper extends SQLiteOpenHelper
{
    public static final String DB_NAME = "intercom_db";

   public IntercomDBHelper(Context context)
   {
       super(context, DB_NAME, null, 1);
   }
 

   @Override
   public void onCreate(SQLiteDatabase db)
   {
       RoomDao roomDao = new RoomDao(this);
       roomDao.createTable();

       PersonnelDao personnelDao = new PersonnelDao(this);
       personnelDao.createTable();
   }
 

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
   {
       RoomDao roomDao = new RoomDao(this);
       roomDao.dropTable();

       PersonnelDao personnelDao = new PersonnelDao(this);
       personnelDao.dropTable();

       onCreate(db);
   }
}
	
