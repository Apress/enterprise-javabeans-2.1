package ejb.testcases;

import ejb.migration.*;
import ejb.test.*;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.ejb.CreateException;


public class TestMigrationBean extends EJBTestCase {
    
    public static final String JDBC_DRIVER = "jdbc.driver";
    public static final String JDBC_URL    = "jdbc.url";
    public static final String JDBC_USER   = "jdbc.user";
    public static final String JDBC_PWD    = "jdbc.pwd";

    private MigrationHome migrationHome;

    public TestMigrationBean() {
        super();
    }


    public void prepareTest()
        throws Exception
    {
        migrationHome = (MigrationHome)narrow("Migration", MigrationHome.class);
        Class.forName(this.getProperties().getProperty(JDBC_DRIVER));
        this.tableDelete();
    }


    public void testCreateAndRemove()
        throws Exception
    {
        Migration migration = migrationHome.create();
        assertNotNull("migration", migration);
        migration.remove();
    }


    public void testMigration()
        throws Exception
    {
        Invoice[] invoices = new Invoice[10];
        for(int i = 1; i <= invoices.length; i++) {
            invoices[i - 1] = new Invoice(i, (float)(i * 100), "DEM");
        }
        this.tableInsert(invoices);

        Migration migration = this.migrationHome.create();
        migration.migrate();
        migration.remove();

        Invoice[] ii = this.tableRead();
        Arrays.sort(ii, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Invoice)o1).id - ((Invoice)o2).id;
            }
        });

        assertEquals("array-length", invoices.length, ii.length);
        for(int i = 0; i < invoices.length; i++) {
            assertEquals(i + ": id", invoices[i].id, ii[i].id);
            assertEquals(i + ": amount", invoices[i].amount / MigrationBean.EXCHANGE_RATE, ii[i].amount);
            assertEquals(i + ": currency", "EU", ii[i].currency);
        }
    }


    public void finalizeTest()
        throws Exception
    {
    }


    private static final String INSERT =
        "INSERT INTO INVOICE" +
        "(ID, AMOUNT, CURRENCY) VALUES(?, ?, ?)";

    private void tableInsert(Invoice[] invoices)
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
            for(int i = 0; i < invoices.length; i++) {
                st.setInt(1, invoices[i].id);
                st.setFloat(2, invoices[i].amount);
                st.setString(3, invoices[i].currency);
                st.executeUpdate();
            }
        } finally {
            try { st.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }

    private static final String SELECT =
        "SELECT ID, AMOUNT, CURRENCY FROM INVOICE";

    private Invoice[] tableRead()
        throws SQLException
    {
        Connection con = null;
        Statement  st  = null;
        ResultSet  rs  = null;
        try {
            String url  = this.getProperties().getProperty(JDBC_URL);
            String user = this.getProperties().getProperty(JDBC_USER);
            String pwd  = this.getProperties().getProperty(JDBC_PWD);
            con = DriverManager.getConnection(url, user, pwd);
            st = con.createStatement();
            rs = st.executeQuery(SELECT);
            ArrayList al = new ArrayList();
            while(rs.next()) {
                Invoice in = new Invoice(rs.getInt(1),
                                         rs.getFloat(2),
                                         rs.getString(3));
                al.add(in);
            }
            return (Invoice[])al.toArray(new Invoice[0]);
        } finally {
            try { rs.close();  } catch(Exception ex) {}
            try { st.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }

    private static final String DELETE =
        "DELETE FROM INVOICE";

    private void tableDelete()
    {
        Connection con = null;
        Statement  st  = null;
        try {
            String url  = this.getProperties().getProperty(JDBC_URL);
            String user = this.getProperties().getProperty(JDBC_USER);
            String pwd  = this.getProperties().getProperty(JDBC_PWD);
            con = DriverManager.getConnection(url, user, pwd);
            st = con.createStatement();
            st.executeQuery(DELETE);
        } catch(SQLException ex) {
            //ignore
        } finally {
            try { st.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }


    public static class Invoice {

        public int id;
        public float amount;
        public String currency;

        public Invoice(int id, float amount, String currency) {
            this.id = id;
            this.amount = amount;
            this.currency = currency;
        }

    }


}
