package ejb.store;

import javax.ejb.EJBObject;

import java.rmi.RemoteException;
import java.util.Collection;


public interface Store extends EJBObject {
    
    public Integer getStoreId()
        throws RemoteException;
    
    public String getStoreName()
        throws RemoteException;
    
    public Collection getStoreRacks()
        throws RemoteException;
    
    public Collection getStoreRackPositions(Rack rack)
        throws RemoteException;
    
    public Item getItemInPosition(Position pos)
        throws RemoteException;

    public Collection getAllStoreItems()
        throws RemoteException;

    public Collection getPositionsForItem(Item item)
        throws RemoteException;
    
}
