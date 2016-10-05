package ejb.part;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

public interface Part extends EJBObject {

    //public void setPartDetails(PartDetails pd)
    public TsPartDetails setPartDetails(TsPartDetails pd)
        throws RemoteException, OutOfDateException;
    
    public TsPartDetails getPartDetails()
        throws RemoteException;

}
