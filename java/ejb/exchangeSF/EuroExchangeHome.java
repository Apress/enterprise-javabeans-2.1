package ejb.exchangeSF;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface EuroExchangeHome extends EJBHome {

    public EuroExchange create(String foreignCurr) 
        throws RemoteException, CreateException;

}
