package gid.cs.huji.intercom.json_deserialize;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Date;
import java.util.Map;

import gid.cs.huji.intercom.model.Personnel;

/**
 * Created by gideonbar on 10/03/15.
 */
public class JsonToPersonnel
{
    private static final String TAG = JsonToPersonnel.class.getSimpleName();

    public static final String KEY_ID_ORDER = "id_order";
    public static final String KEY_PERSONNEL = "personnel";
    public static final String KEY_UPDATE = "update";

    public static final String KEY_PERSON_NAME = "name";
    public static final String KEY_PERSON_SURNAME = "surname";
    public static final String KEY_ROOM = "room";
    public static final String KEY_PATH = "path";


    public void deserialize(String j)
    {
        try
        {
            JsonObject jo_map = new JsonParser().parse(j).getAsJsonObject();

            JsonArray ja_id_order = jo_map.get(KEY_ID_ORDER).getAsJsonArray();
            JsonObject jo_personnel = jo_map.get(KEY_PERSONNEL).getAsJsonObject();
            String date = jo_map.get(KEY_UPDATE).getAsString();

            Gson gson = new Gson();

            for (JsonElement e : ja_id_order)
            {
                JsonObject jo_person = jo_personnel.get(e.getAsString()).getAsJsonObject();

                String name = jo_person.get(KEY_PERSON_NAME).getAsString();
//                Log.d(TAG, name);

                String surname = jo_person.get(KEY_PERSON_SURNAME).getAsString();
//                Log.d(TAG, surname);

                String path = jo_person.get(KEY_PATH).getAsString();
//                Log.d(TAG, path);

                String room = jo_person.get(KEY_ROOM).getAsString();
//                Log.d(TAG, room);

                Personnel personnel = new Personnel(name, surname, path, room);

                Log.d(TAG, personnel.getBrowseText());
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "", e);
        }
    }
}
