package ejb.supplychain.stock;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

public interface Stock extends EJBObject {

    public void get(int amount) 
        throws ProcessingErrorException, RemoteException;

    public void put(int amount) 
        throws ProcessingErrorException, RemoteException;

    public int  getVolume() 
        throws RemoteException;

}
