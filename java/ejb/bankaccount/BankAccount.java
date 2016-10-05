package ejb.bankaccount;

import java.rmi.RemoteException;
import javax.ejb.*;

public interface BankAccount extends EJBObject {
    //Erfragen der Kontonummer
    public String getAccNumber()
        throws RemoteException;
    //Erfragen der Kontobezeichnung
    public String getAccDescription()
        throws RemoteException;
    //Erfragen des Kontostandes
    public float getBalance()
        throws RemoteException;
    //Erhöhen des Kontostandes
    public void increaseBalance(float amount)
        throws RemoteException;
    //Vermindern des Kontostandes
    public void decreaseBalance(float amount)
        throws RemoteException;
}