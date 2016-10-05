package ejb.testcases;

import ejb.exchangeSL.*;
import ejb.test.*;

import java.rmi.RemoteException;


public class TestExchangeSLBean extends EJBTestCase {
    

    private EuroExchangeHome exchangeHome;

    public TestExchangeSLBean() {
        super();
    }


    public void prepareTest()
        throws Exception
    {
        exchangeHome = (EuroExchangeHome)this.narrow("ExchangeSL", EuroExchangeHome.class);
    }


    public void testCreateAndRemove()
        throws Exception
    {
        EuroExchange exchange = exchangeHome.create();
        assertNotNull("exchange", exchange);
        exchange.remove();
    }


    public void testConstraints()
        throws Exception
    {
        EuroExchange exchange = exchangeHome.create();
        try {
            exchange.changeFromEuro(null, 100.0f);
            fail("changeFromEuro: expected RemoteException, curr is null");
        } catch(RemoteException rex) {
            //as expected
        }
        try {
            exchange.changeFromEuro("UNKNOWN-CURRENCY", 100.0f);
            fail("changeFromEuro: expected RemoteException, curr is unknown");
        } catch(RemoteException rex) {
            //as expected
        }
        try {
            exchange.changeToEuro(null, 100.0f);
            fail("changeToEuro: expected RemoteException, curr is null");
        } catch(RemoteException rex) {
            //as expected
        }
        try {
            exchange.changeToEuro("UNKNOWN-CURRENCY", 100.0f);
            fail("changeToEuro: expected RemoteException, curr is unknown");
        } catch(RemoteException rex) {
            //as expected
        }
        try {
            exchange.setExchangeRate(null, 100.0f, 100.0f);
            fail("setExchangeRate: expected RemoteException, curr is null");
        } catch(RemoteException rex) {
            //as expected
        }
        exchange.remove();
    }


    public void testExchange()
        throws Exception
    {
        EuroExchange exchange = exchangeHome.create();

        final String curr = "DEM-" + System.currentTimeMillis();

        exchange.setExchangeRate(curr, 1f/1.92f, 1.92f);

        final float dem = 100f;

        final float euro = exchange.changeToEuro(curr, dem);

        final float dem1 = exchange.changeFromEuro(curr, euro);

        assertEquals("dem after conversion", Math.round(dem), Math.round(dem1));

    }


    public void finalizeTest()
        throws Exception
    {
    }

}
