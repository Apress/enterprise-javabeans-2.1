package ejb.exchangeSL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class EuroExchangeBean implements SessionBean {

    public static final String dbRef = "java:comp/env/jdbc/EuroDB";

    private SessionContext beanCtx    = null;
    private DataSource     dataSource = null;
  

    public void ejbCreate()
        throws CreateException
    {
        try { 
            Context c = new InitialContext();
            this.dataSource = (DataSource)c.lookup(dbRef);
        } catch(NamingException ex) {
            String msg = "Cannot get Resource-Factory:" + ex.getMessage();
            throw new CreateException(msg);
        }
    }


    public void ejbRemove() {
        this.dataSource = null;
    }


    public float changeFromEuro(String currency, float amount) {
        if(currency == null) {
            throw new EJBException("illegal argument: currency");
        }
        final String query = 
            "SELECT FOREIGNCURR FROM EURO_EXCHANGE WHERE CURRENCY=?";
        Connection        con = null;
        PreparedStatement st  = null;
        ResultSet         rs  = null;
        try {
            con = this.dataSource.getConnection();
            st  = con.prepareStatement(query);
            st.setString(1, currency);
            rs = st.executeQuery();
            if(!rs.next()) {
                throw new EJBException("no such currency:" + currency);
            }
            return amount * rs.getFloat(1);
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new EJBException("db-error:" + ex.getMessage());
        } finally {
            try { rs.close();  } catch(Exception ex) {}
            try { st.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }


    public float changeToEuro(String currency, float amount) {
        if(currency == null) {
            throw new EJBException("illegal argument: currency");
        }
        final String query = 
            "SELECT EURO FROM EURO_EXCHANGE WHERE CURRENCY=?";
        Connection        con = null;
        PreparedStatement st  = null;
        ResultSet         rs  = null;
        try {
            con = this.dataSource.getConnection();
            st  = con.prepareStatement(query);
            st.setString(1, currency);
            rs = st.executeQuery();
            if(!rs.next()) {
                throw new EJBException("no such currency:" + currency);
            }
            return amount * rs.getFloat(1);
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new EJBException("db-error:" + ex.getMessage());
        } finally {
            try { rs.close();  } catch(Exception ex) {}
            try { st.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }


    public void setExchangeRate(String currency,
                                float  euro,
                                float  foreignCurr)
    {
        if(currency == null) {
            throw new EJBException("illegal argument: currency");
        }
//        this.checkSecureAccess();
        final String delQuery = 
            "DELETE FROM EURO_EXCHANGE WHERE CURRENCY=?";
        final String insQuery = 
            "INSERT INTO EURO_EXCHANGE" +
            "(CURRENCY, EURO, FOREIGNCURR) VALUES(?, ?, ?)";
        Connection        con = null;
        PreparedStatement del = null;
        PreparedStatement ins = null;
        boolean success = false;
        try {
            con = this.dataSource.getConnection();
            con.setAutoCommit(false);
            del = con.prepareStatement(delQuery);
            del.setString(1, currency);
            del.executeUpdate();
            ins = con.prepareStatement(insQuery);
            ins.setString(1, currency);
            ins.setFloat(2, euro);
            ins.setFloat(3, foreignCurr);
            ins.executeUpdate();
            success = true;
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new EJBException("db-error:" + ex.getMessage());
        } finally {
            if(success) {
                try { con.commit(); } catch(Exception ex) {}
            } else {
                try { con.rollback(); } catch(Exception ex) {}
            }
            try { del.close(); } catch(Exception ex) {}
            try { ins.close(); } catch(Exception ex) {}
            try { con.setAutoCommit(true); } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }



    private void checkSecureAccess() {
        java.security.Principal principal;
        String name;
        boolean mayAccess;
        
        mayAccess = beanCtx.isCallerInRole("setCurrency");
        principal = beanCtx.getCallerPrincipal();
        name = principal.getName();
        
        if(mayAccess){
            System.err.println("Accessed by "+name);
        }
        else{
            System.err.println("Blocked access by "+name);
        }
        
        if(!mayAccess) {
            throw new EJBException(principal.getName() + ": access denied");
        }
    }


    public void ejbPassivate() { }

    public void ejbActivate() { }

    public void setSessionContext(SessionContext ctx) {
        this.beanCtx = ctx;
    }

}
