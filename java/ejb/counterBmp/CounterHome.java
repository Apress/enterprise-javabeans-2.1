package ejb.counterBmp;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;


public interface CounterHome extends EJBHome {

    public Counter create(String counterId, int initCounterValue) 
        throws CreateException, RemoteException;

    public Counter findByPrimaryKey(String primaryKey) 
        throws FinderException, RemoteException;

    public java.util.Collection findAllCounters()
        throws FinderException, RemoteException;
       
}
