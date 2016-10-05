package ejb.exchangeSF.client;

import ejb.exchangeSF.EuroExchange;
import ejb.exchangeSF.EuroExchangeHome;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;


public class Client {

    private EuroExchange exchange;

    public Client() { }


    public void init(String curr)
        throws NamingException, RemoteException, CreateException
    {
        java.util.Properties p = new java.util.Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        p.put(Context.PROVIDER_URL, "t3://localhost:7001");
        Context ctx = new InitialContext(p);

        Object o = ctx.lookup("ExchangeSF");
        EuroExchangeHome home = (EuroExchangeHome)PortableRemoteObject.narrow(o, EuroExchangeHome.class);
        this.exchange = home.create(curr);
        System.out.println("init successful");
    }


    public void run() {
        System.out.println("Changing 100 US-Dollars to Euro");
        float amount = 0f;
        try {
            amount = this.exchange.changeToEuro(100);
        } catch(RemoteException ex) {
            ex.printStackTrace();
            return;
        }
        System.out.println("Result: " + amount);

        System.out.println("Changing " + amount + " Euro to US-Dollars");
        float n_amount = 0f;
        try {
            n_amount = this.exchange.changeFromEuro(amount);
        } catch(RemoteException ex) {
            ex.printStackTrace();
            return;
        }
        System.out.println("Result: " + n_amount);
    }


    public void cleanUp() {
        try {
            this.exchange.remove();
        } catch(RemoteException ex) {
            ex.printStackTrace();
        } catch(RemoveException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Client c = new Client();
        try {
            c.init("US-Dollar");
        } catch(Exception ex) {
            ex.printStackTrace();
            return;
        }
        c.run();
        c.cleanUp();
    }

}
