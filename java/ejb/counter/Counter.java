package ejb.counter;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;


public interface Counter extends EJBObject {

    /** Incrementiert den Zaehler */
    public void inc()
        throws RemoteException, CounterOverflowException;

    /** Decrementiert den Zaehler */
    public void dec()
        throws RemoteException, CounterOverflowException;

    /** Liefert den aktuellen Wert des Zaehlers */
    public int getValue()
        throws RemoteException;

}
