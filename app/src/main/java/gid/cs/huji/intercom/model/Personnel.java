package gid.cs.huji.intercom.model;

import gid.db_util.CommonColumnNames;
import gid.interfaces.IBrowsable;

public class Personnel implements IBrowsable
{

    public static final String PERSONNEL = "personnel";
    public static final String SURNAME = "surname";
    public static final String PATH = "path";
    public static final String ROOM_ID = Room.ROOM + CommonColumnNames._ID;
    public static final String ID_ORDER = "id_order";


    private Integer id;
    private int server_id;
    private String name;
    private String surname;
    private String path;
    private Room room;

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
}
