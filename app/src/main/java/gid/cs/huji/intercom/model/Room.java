package gid.cs.huji.intercom.model;

import java.io.Serializable;

import gid.interfaces.IBrowsable;

/**
 * Created by gideonbar on 11/03/15.
 */
public class Room implements IBrowsable, Serializable
{
    public static final String ROOM = "room";
    public static final String BUILDING = "building";
    public static final String WING = "wing";
    public static final String FLOOR = "floor";
    public static final String NUM = "num";
    public static final String ROOMS = "rooms";


    private Integer id;
    private int server_id;
    private String building;
    private String wing;
    private int floor;
    private int num;

    public Room(){}


    public Room(Integer id, int server_id, String building, String wing, int floor, int num) {
        this.id = id;
        this.server_id = server_id;
        this.building = building;
        this.wing = wing;
        this.floor = floor;
        this.num = num;
    }

    public Integer getId()
    {
        if(id==null || id == 0)
        {

        }

        return id;
    }

    public int getServer_id() {
        return server_id;
    }

    public String getBuilding() {
        return building;
    }

    public String getWing() {
        return wing;
    }

    public int getFloor() {
        return floor;
    }

    public int getNum() {
        return num;
    }

    public String getName()
    {
        return this.wing + " " + this.floor + (this.num<10?"0"+this.num:this.num);
    }

    @Override
    public String getBrowseImagePath() {
        return null;
    }

    @Override
    public String getBrowseText() {
        return this.getName();
    }

    public void setId(long id) {
        this.id = (int)id;
    }
}
