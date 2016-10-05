package ejb.payroll;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;


public interface Payroll extends EJBObject {

    public void processPayroll() throws RemoteException;

}
