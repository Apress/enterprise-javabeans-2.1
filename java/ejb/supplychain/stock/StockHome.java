package ejb.supplychain.stock;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;

public interface StockHome extends EJBHome {

    public Stock create(String stockId, int maxVolume, int aktVolume) 
        throws CreateException, RemoteException;

    public Stock findByPrimaryKey(String primaryKey) 
        throws FinderException, RemoteException;

}
