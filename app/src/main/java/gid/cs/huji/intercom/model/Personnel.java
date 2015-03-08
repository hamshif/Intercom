package gid.cs.huji.intercom.model;

import gid.interfaces.IBrowsable;

public class Personnel implements IBrowsable
{
    private String name;
    private String surname;
    private String imagePath;
    private String room;

    public Personnel(String name, String surname, String imagePath, String room)
    {
        this.name = name;
        this.surname = surname;
        this.imagePath = imagePath;
        this.room = room;
    }

    @Override
    public String getBrowseImagePath()
    {
        return this.imagePath;
    }

    @Override
    public String getBrowseText()
    {
        return this.name + " " + this.surname;
    }

    public String getRoom()
    {
        return room;
    }
}
