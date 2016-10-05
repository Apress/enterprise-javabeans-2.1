package ejb.migration;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;


public interface Migration extends EJBObject {

    public void migrate()
        throws MigrationErrorException, RemoteException;

}
