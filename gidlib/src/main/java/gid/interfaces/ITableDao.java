package gid.interfaces;

import java.util.List;

/**
 * Created by gideonbar on 12/03/15.
 */
public interface ITableDao<A extends Object>
{
    public void closeDB();
    public void createTable();
    public void dropTable();
    public void persistObject(A obj);
    public A getModelObjects(String [] ids);
}
