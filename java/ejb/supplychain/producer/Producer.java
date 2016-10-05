package ejb.supplychain.producer;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

public interface Producer extends EJBObject {

    public int produce(int amount)
        throws UnappropriateStockException, RemoteException;

}
