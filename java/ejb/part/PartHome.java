package ejb.part;

import java.rmi.RemoteException;
import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface PartHome extends EJBHome {

    public Part create(String partNumber)
        throws CreateException, RemoteException;
    
    public Part findByPrimaryKey(String partNumber)
        throws FinderException, RemoteException;
    
}