package gid.cs.huji.intercom.model;

import java.io.Serializable;

import gid.db_util.CommonKeys;
import gid.interfaces.IBrowsable;

public class Personnel implements IBrowsable, Serializable
{

    public static final String PERSONNEL = "personnel";
    public static final String SURNAME = "surname";
    public static final String PATH = "path";
    public static final String ROOM_ID = Room.ROOM + CommonKeys._ID;
    public static final String ID_ORDER = "id_order";
    public static final String PERSONNELS = "personnels";


    private Integer id;
    private int server_id;
    private String name;
    private String surname;
    private String path;
    private Room room;

    public Personnel(){}



    public Personnel(Integer id, int server_id, String name, String surname, String path, Room room)
    {
        this.id = id;
        this.server_id = server_id;
        this.name = name;
        this.surname = surname;
        this.path = path;
        this.room = room;
    }

    @Override
    public String getBrowseImagePath()
    {
        return this.path;
    }

    @Override
    public String getBrowseText()
    {
        return this.name + " " + this.surname;
    }

    public Integer getId() {
        return id;
    }

    public int getServerId() {
        return server_id;
    }

    public Room getRoom()
    {
        return room;
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getPath() {
        return path;
    }


    public void setId(long id) {
        this.id = (int)id;
    }
}
