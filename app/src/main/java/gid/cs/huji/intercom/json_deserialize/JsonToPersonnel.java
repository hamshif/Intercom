package gid.cs.huji.intercom.json_deserialize;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gid.cs.huji.intercom.model.Personnel;
import gid.cs.huji.intercom.model.Room;
import gid.db_util.CommonColumnNames;

/**
 * Created by gideonbar on 10/03/15.
 */
public class JsonToPersonnel
{
    private static final String TAG = JsonToPersonnel.class.getSimpleName();


    public void deserialize(String j)
    {
        try
        {
            JsonObject jo_map = new JsonParser().parse(j).getAsJsonObject();

            JsonObject jo_rooms = jo_map.get("rooms").getAsJsonObject();
            JsonArray ja_id_order = jo_map.get(Personnel.ID_ORDER).getAsJsonArray();
            JsonObject jo_personnel = jo_map.get(Personnel.PERSONNEL).getAsJsonObject();
            String s_date = jo_map.get(CommonColumnNames.UPDATE).getAsString();

            Gson gson = new Gson();

            JsonObject jo_room;

            int server_id;
            String building;
            String wing;
            int floor;
            int num;

            HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
            Room room;

            for (Map.Entry<String, JsonElement> entry : jo_rooms.entrySet())
            {

                Log.d(TAG, entry.getKey() + "/" + entry.getValue());

                jo_room = entry.getValue().getAsJsonObject();

                server_id = Integer.parseInt(entry.getKey());
                building = jo_room.get(Room.BUILDING).getAsString();
                wing = jo_room.get(Room.WING).getAsString();
                floor = Integer.parseInt(jo_room.get(Room.FLOOR).getAsString());
                num = Integer.parseInt(jo_room.get(Room.NUM).getAsString());

                room = new Room(null, server_id, building, wing, floor, num);

                Log.d(TAG, room.getName());

                rooms.put(server_id, room);
            }

            String name;
            String surname;
            String path;

            JsonElement e;

            for (int i=0; i<ja_id_order.size(); i++)
            {
                e = ja_id_order.get(i);

                server_id = ja_id_order.get(i).getAsInt();

                JsonObject jo_person = jo_personnel.get(e.getAsString()).getAsJsonObject();

                name = jo_person.get(CommonColumnNames.NAME).getAsString();
//                Log.d(TAG, name);

                surname = jo_person.get(Personnel.SURNAME).getAsString();
//                Log.d(TAG, surname);

                path = jo_person.get(Personnel.PATH).getAsString();
//                Log.d(TAG, path);

                room = rooms.get(new Integer(jo_person.get(Room.ROOM).getAsInt()));
//                Log.d(TAG, room);

                Personnel personnel = new Personnel(null, server_id, name, surname, path, room);

                Log.d(TAG, personnel.getBrowseText());
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "", e);
        }
    }
}
