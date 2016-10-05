package ejb.supplychain.producer;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public interface ProducerHome extends EJBHome {

    public Producer create() 
        throws CreateException, RemoteException;

}
