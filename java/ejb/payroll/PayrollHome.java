package ejb.payroll;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface PayrollHome extends EJBHome {

    public Payroll create()
        throws RemoteException, CreateException;

}
