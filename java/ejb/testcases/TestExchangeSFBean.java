package ejb.testcases;

import ejb.exchangeSF.*;
import ejb.test.*;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.ejb.CreateException;


public class TestExchangeSFBean extends EJBTestCase {
    
    public static final String JDBC_DRIVER = "jdbc.driver";
    public static final String JDBC_URL    = "jdbc.url";
    public static final String JDBC_USER   = "jdbc.user";
    public static final String JDBC_PWD    = "jdbc.pwd";

    private EuroExchangeHome exchangeHome;

    public TestExchangeSFBean() {
        super();
    }


    public void prepareTest()
        throws Exception
    {
        exchangeHome = (EuroExchangeHome)narrow("ExchangeSF", EuroExchangeHome.class);
        Class.forName(this.getProperties().getProperty(JDBC_DRIVER));
    }


    public void testCreateAndRemove()
        throws Exception
    {
        try {
            exchangeHome.create(null);
        } catch(CreateException ex) {
            //as expected
        }
        try {
            exchangeHome.create("UNKNOWN_CURRENCY");
        } catch(CreateException ex) {
            //as expected
        }
        final String currency = System.currentTimeMillis() + "-DEM";
        this.tableInsert(currency, 1/1.8f, 1.8f);
        EuroExchange exchange = exchangeHome.create(currency);
        assertNotNull("exchange", exchange);
        exchange.remove();
    }


    public void testExchange()
        throws Exception
    {
        final String currency = System.currentTimeMillis() + "-DEM";
        final float euroRate = 1/1.8f;
        final float currRate = 1.8f;

        this.tableInsert(currency, euroRate, currRate);

        EuroExchange exchange = exchangeHome.create(currency);
        assertNotNull("exchange", exchange);

        final float amount = 100f;
        final float val1 = exchange.changeToEuro(amount);
        final float val2 = exchange.changeFromEuro(val1);
        assertEquals("exchange value", Math.round(amount), Math.round(val2));

        final float amount1 = 222f;
        final float val3 = exchange.changeToEuro(amount1);
        final float val4 = exchange.changeFromEuro(val3);
        assertEquals("exchange value", Math.round(amount1), Math.round(val4));

        final String currency1 = System.currentTimeMillis() + "-DEM";
        final float euroRate1 = 1/1.2f;
        final float currRate1 = 1.2f;

        this.tableInsert(currency1, euroRate1, currRate1);
        exchange.setForeignCurr(currency1);
        final float amount2 = 444f;
        final float val5 = exchange.changeToEuro(amount2);
        final float val6 = exchange.changeFromEuro(val5);
        assertEquals("exchange value", Math.round(amount2), Math.round(val6));

        exchange.remove();
    }


    public void finalizeTest()
        throws Exception
    {
    }


    private static final String INSERT =
        "INSERT INTO EURO_EXCHANGE" +
        "(CURRENCY, EURO, FOREIGNCURR) VALUES(?, ?, ?)";

    private void tableInsert(String currency, float euro, float foreignCurr)
        throws SQLException
    {
        Connection        con = null;
        PreparedStatement st  = null;
        try {
            String url  = this.getProperties().getProperty(JDBC_URL);
            String user = this.getProperties().getProperty(JDBC_USER);
            String pwd  = this.getProperties().getProperty(JDBC_PWD);
            con = DriverManager.getConnection(url, user, pwd);
            st = con.prepareStatement(INSERT);
            st.setString(1, currency);
            st.setFloat(2, euro);
            st.setFloat(3, foreignCurr);
            st.executeUpdate();
        } finally {
            try { st.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }


}
