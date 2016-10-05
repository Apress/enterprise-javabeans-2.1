package ejb.exchangeSF;

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

    private String foreignCurr;
    private float  euroRate;
    private float  currRate;

    private SessionContext beanCtx    = null;

    private transient DataSource dataSource = null;
  

    public void ejbCreate(String foreignCurr)
        throws CreateException
    {
        if(foreignCurr == null) {
            throw new CreateException("foreignCurr is null");
        }
        try { 
            Context c = new InitialContext();
            this.dataSource = (DataSource)c.lookup(dbRef);
        } catch(NamingException ex) {
            String msg = "Cannot get Resource-Reference:" + ex.getMessage();
            throw new CreateException(msg);
        }
        this.foreignCurr = foreignCurr;
        this.euroRate    = 0f;
        this.currRate    = 0f;
        try {
            this.readCurrencyValues();
        } catch(EJBException ex) {
            throw new CreateException(ex.getMessage());
        }
    }


    public void ejbRemove() {
        this.dataSource  = null;
        this.foreignCurr = null;
        this.euroRate    = 0f;
        this.currRate    = 0f;
    }


    public float changeFromEuro(float amount) {
        return amount * this.currRate;
    }


    public float changeToEuro(float amount) {
        return amount * this.euroRate;
    }


    public void  setForeignCurr(String foreignCurr) {
        if(foreignCurr == null) {
            throw new EJBException("foreignCurr is null!");
        }
        this.foreignCurr = foreignCurr;
        this.euroRate    = 0f;
        this.currRate    = 0f;
        this.readCurrencyValues();
    }


    private void readCurrencyValues()
        throws EJBException
    {
        if(this.foreignCurr == null) {
            throw new EJBException("foreignCurr not set");
        }
        final String query = 
            "SELECT EURO, FOREIGNCURR FROM EURO_EXCHANGE WHERE CURRENCY=?";
        Connection        con = null;
        PreparedStatement st  = null;
        ResultSet         rs  = null;
        try {
            con = this.dataSource.getConnection();
            st  = con.prepareStatement(query);
            st.setString(1, this.foreignCurr);
            rs = st.executeQuery();
            if(!rs.next()) {
                throw new EJBException("no such currency:" + this.foreignCurr);
            }
            this.euroRate = rs.getFloat(1);
            this.currRate = rs.getFloat(2);
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new EJBException("db-error:" + ex.getMessage());
        } finally {
            try { rs.close();  } catch(Exception ex) {}
            try { st.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }


    public void ejbPassivate() {
        this.dataSource = null;
    }


    public void ejbActivate() {
        try { 
            Context c = new InitialContext();
            this.dataSource = (DataSource)c.lookup(dbRef);
        } catch(NamingException ex) {
            String msg = "Cannot get Resource-Reference:" + ex.getMessage();
            throw new EJBException(msg);
        }
    }


    public void setSessionContext(SessionContext ctx) {
        this.beanCtx = ctx;
    }

}
