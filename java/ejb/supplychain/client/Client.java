package ejb.supplychain.client;

import ejb.supplychain.stock.Stock;
import ejb.supplychain.stock.StockHome;
import ejb.util.Lookup;

import javax.transaction.UserTransaction;


public class Client {

    private final static String STOCK_HOME = "Stock";
    private final static String USER_TA    = "javax.transaction.TransactionManager";

    private UserTransaction userTx = null;

    private StockHome stockHome = null;

    private Stock stock1;
    private Stock stock2;
    private Stock stock3;


    public Client() {
        try {
            this.userTx    = (UserTransaction) Lookup.narrow(USER_TA, UserTransaction.class);
            this.stockHome = (StockHome) Lookup.narrow(STOCK_HOME, StockHome.class);
        } catch(javax.naming.NamingException ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex.getMessage());
        }
    }


    public void preconditions() 
        throws java.rmi.RemoteException
    {
        this.stock1 = this.createStock("stock1", 100, 100); 
        this.stock2 = this.createStock("stock2", 100, 100); 
        this.stock3 = this.createStock("stock3", 100, 0); 

        System.out.println("Stock1 created. Current Volume: " + this.stock1.getVolume());
        System.out.println("Stock2 created. Current Volume: " + this.stock2.getVolume());
        System.out.println("Stock3 created. Current Volume: " + this.stock3.getVolume());
    }


    private Stock createStock(String name, int max, int cap)
        throws java.rmi.RemoteException
    {
        Stock stock = null;
        try {
            stock = this.stockHome.findByPrimaryKey(name);
            stock.remove();
        } catch(javax.ejb.FinderException ex) {
            //do nothing
        } catch(javax.ejb.RemoveException ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex.getMessage());
        }
        try {
            stock = this.stockHome.create(name, max, cap);
        } catch(javax.ejb.CreateException ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex.getMessage());
        }
        return stock;
    }


    public void doProduction(int volume) 
        throws java.rmi.RemoteException
    {
        boolean rollback = true;
        try {
            this.userTx.begin();
            System.out.println("Producing " + volume + " units ...");
            this.stock1.get(volume);
            this.stock2.get(volume*2);
            this.stock3.put(volume);
            System.out.println("done.");
            rollback = false;
        } catch(Exception ex) {
            System.out.println("FAILED.");
            System.err.println(ex.toString());
        } finally {
            if(!rollback) {
                try { this.userTx.commit(); } catch(Exception ex) {}
            } else {
                try { this.userTx.rollback(); } catch(Exception ex) {}
            }
        }
        System.out.println("Stock1 Volume: " + this.stock1.getVolume());
        System.out.println("Stock2 Volume: " + this.stock2.getVolume());
        System.out.println("Stock3 Volume: " + this.stock3.getVolume());
    }


    public static void main(String[] args)
        throws java.rmi.RemoteException
    {
        Client c = new Client();
        c.preconditions();
        c.doProduction(10);
        c.doProduction(20);
        c.doProduction(50);
    }


}
