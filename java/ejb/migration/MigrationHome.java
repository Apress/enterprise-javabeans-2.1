package ejb.migration;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public interface MigrationHome extends EJBHome {

    public Migration create() 
        throws CreateException, RemoteException;

}
