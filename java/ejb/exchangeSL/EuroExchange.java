package ejb.exchangeSL;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;


public interface EuroExchange extends EJBObject {

    public float changeFromEuro(String currency, float amount) 
        throws RemoteException;

    public float changeToEuro(String currency, float amount) 
        throws RemoteException;
  	
    public void setExchangeRate(String currency, float euro, float foreignCurr)
        throws RemoteException;

}
