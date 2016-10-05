package ejb.supplychain.producer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.naming.NamingException;

import ejb.supplychain.stock.ProcessingErrorException;
import ejb.supplychain.stock.Stock;
import ejb.supplychain.stock.StockHome;
import ejb.util.Lookup;


public class ProducerBean implements SessionBean, SessionSynchronization {

    public static final String STOCK_HOME = "java:comp/env/ejb/Stock";
    public static final String SOURCE_ID1 = "java:comp/env/idSource1";
    public static final String SOURCE_ID2 = "java:comp/env/idSource2";
    public static final String TARGET_ID  = "java:comp/env/idTarget";

    private Stock source1;
    private Stock source2;
    private Stock target;

    private SessionContext sessionCtxt = null;
	
    public void ejbCreate()
        throws CreateException
    {
        try {
            this.openRessources();
        } catch(Exception ex) {
            throw new CreateException(ex.getMessage());
        }
    }
  
    public void ejbRemove() {
        this.source1 = null;
        this.source2 = null;
        this.target  = null;
    }
  
    public void ejbActivate() {
        try {
            this.openRessources();
        } catch(Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
	
    public void ejbPassivate() {
        this.source1 = null;
        this.source2 = null;
        this.target  = null;
    }
 
    public void setSessionContext(SessionContext ctxt) {
        sessionCtxt = ctxt;
    }

    public int produce(int amount)
        throws UnappropriateStockException
    {
        int ret;
        try {
            System.out.println("starting produce");
            this.source1.get(amount);
            this.source2.get(amount*2);
            this.target.put(amount);
            ret = amount;
        } catch(ProcessingErrorException e) {
            sessionCtxt.setRollbackOnly();
            throw new UnappropriateStockException();
        } catch (java.rmi.RemoteException re) {
            sessionCtxt.setRollbackOnly();
            throw new UnappropriateStockException();
        }
        return ret;
    }

    private void openRessources()
        throws FinderException, NamingException
    {
        try {
            // get stock IDs
            String idSource1 = (String)Lookup.narrow(SOURCE_ID1, String.class);
            String idSource2 = (String)Lookup.narrow(SOURCE_ID2, String.class);
            String idTarget  = (String)Lookup.narrow(TARGET_ID, String.class);

            // get home
            StockHome stockHome = (StockHome)Lookup.narrow(STOCK_HOME, StockHome.class);

            // get stocks
            this.source1 = stockHome.findByPrimaryKey(idSource1);
            this.source2 = stockHome.findByPrimaryKey(idSource2);
            this.target  = stockHome.findByPrimaryKey(idTarget);
        } catch(java.rmi.RemoteException e) {
            throw new EJBException(e.getClass().getName()
                                   + ": " + e.getMessage());
        }
    }
  
    // Implementation of SessionSynchronisation interface - optional

    public void afterBegin() {
        System.out.println("after begin");
    }

    public void beforeCompletion() {
        System.out.println("before completion");
    }

    public void afterCompletion(boolean committed) {
        System.out.println("after competion");
    }
  
}
