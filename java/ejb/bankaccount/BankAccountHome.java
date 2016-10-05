package ejb.bankaccount;

import java.rmi.RemoteException;
import javax.ejb.*;

public interface BankAccountHome extends EJBHome {
    
    //Erzeugen eines Kontos
    public BankAccount create(String accNo,
                              String accDescription,
                              float  initialBalance)
        throws CreateException, RemoteException;
    
    //Finden eines bestimmten Kontos
    public BankAccount findByPrimaryKey(String accPK)
        throws FinderException, RemoteException;
    
}