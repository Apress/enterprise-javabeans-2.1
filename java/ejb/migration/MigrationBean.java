package ejb.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import ejb.util.Lookup;


public class MigrationBean implements SessionBean {

    public static final float  EXCHANGE_RATE = 1.98f;
    public static final String RESOURCE_REF =
        "java:comp/env/jdbc/Migration";

    private SessionContext sessionCtx;
    private DataSource     dataSource;


    public MigrationBean() { }

    public void ejbCreate()
        throws CreateException
    {
        try {
            this.dataSource = (DataSource)Lookup.narrow(RESOURCE_REF, DataSource.class);
        } catch(Exception ex) {
            String msg = "Cannot get DataSource:"
                       + ex.getMessage();
            throw new EJBException(msg);
        }
    }

    public void ejbRemove() {
        this.dataSource = null;
    }

    public void ejbActivate() { }

    public void ejbPassivate() { }

    public void setSessionContext(SessionContext ctx) {
        this.sessionCtx = ctx;
    }

    public void unsetSessionContext() {
        this.sessionCtx = null;
    }

    private static final String QUERY1 =
        "UPDATE INVOICE SET AMOUNT=AMOUNT/? " +
        "WHERE CURRENCY='DEM'";

    private static final String QUERY2 =
        "UPDATE INVOICE SET CURRENCY='EU' " +
        "WHERE CURRENCY='DEM'";

    public void migrate()
        throws MigrationErrorException
    {
        Connection        con     = null;
        PreparedStatement st1     = null;
        Statement         st2     = null;
        boolean           success = false;
        try {
            con = this.dataSource.getConnection();
            con.setAutoCommit(false);
            st1 = con.prepareStatement(QUERY1);
            st1.setFloat(1, EXCHANGE_RATE);
            st1.executeUpdate();
            st2 = con.createStatement();
            st2.executeUpdate(QUERY2);
            success = true;
        } catch(SQLException ex) {
            String msg = "Failed migrating data:" + ex.getMessage();
            throw new MigrationErrorException(msg);
        } finally {
            if(success) {
                try { con.commit(); } catch(Exception ex) {}
            } else {
                try { con.rollback(); } catch(Exception ex) {}
            }
            try { st1.close(); } catch(Exception ex) {}
            try { st2.close(); } catch(Exception ex) {}
            try { con.setAutoCommit(true); } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }

}
