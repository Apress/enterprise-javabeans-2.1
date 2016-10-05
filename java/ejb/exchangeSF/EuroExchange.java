package ejb.exchangeSF;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;


public interface EuroExchange extends EJBObject {

    public void  setForeignCurr(String foreignCurr) 
        throws RemoteException;
    
    public float changeToEuro(float amount) 
        throws RemoteException;
    
    public float changeFromEuro(float amount) 
        throws RemoteException;
    
}
