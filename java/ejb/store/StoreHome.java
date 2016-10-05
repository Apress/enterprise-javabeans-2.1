package ejb.store;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

import java.rmi.RemoteException;
import java.util.Collection;


public interface StoreHome extends EJBHome {
    
    public Store create(Integer id, String name)
        throws CreateException, RemoteException;
    
    public Store findByPrimaryKey(Integer key)
        throws FinderException, RemoteException;

    public Collection findAllStores()
        throws FinderException, RemoteException;

}
