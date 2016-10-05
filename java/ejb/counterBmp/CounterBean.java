package ejb.counterBmp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;


public class CounterBean implements EntityBean {

    public static final String dbRef = "java:comp/env/jdbc/CounterDB";

    public final static int VALUE_MAX = 100;
    public final static int VALUE_MIN = 0;

    private EntityContext ctx;
    private DataSource    dataSource;

    private String counterId;
    private int    counterValue;

    /**
      * Die Create Methode des Home-Interfaces
      */

    public String ejbCreate(String counterId, int initCounterValue)
        throws CreateException
    {
        if(counterId == null) {
            throw new CreateException("counterId must not be null!");
        }
        if(initCounterValue < VALUE_MIN || initCounterValue > VALUE_MAX) {
            throw new CreateException("initCounterValue out of range!");
        }

        this.initDataSource();

        this.counterId = counterId;
        this.counterValue = initCounterValue;

        try {
            this.create();
        } catch(SQLException ex) {
            throw new CreateException(ex.getMessage());
        }

        return this.counterId;
    }

    public void ejbPostCreate(String accountId, int initCounterValue) {}

    /**
      * Utility-Methoden
      */

    private void initDataSource() {
        if(this.dataSource != null) {
            return;
        }
        try {
           Context c = new InitialContext();
           this.dataSource = (DataSource)c.lookup(dbRef);
        } catch(NamingException ex) {
           String msg = "Cannot get Resource-Factory:" + ex.getMessage();
           throw new EJBException(msg);
        }
    }


    private void create()
        throws SQLException
    {
        final String query =
            "INSERT INTO COUNTER(ID, VALUE) VALUES(?, ?)";
        Connection        con = null;
        PreparedStatement st  = null;
        try {
            con = this.dataSource.getConnection();
            st  = con.prepareStatement(query);
            st.setString(1, this.counterId);
            st.setInt(2, this.counterValue);
            st.executeUpdate();
        } finally {
            try { st.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }

    /**
      * Die Finder-Methoden des Home-Interfaces
      */

    public String ejbFindByPrimaryKey(String pk)
        throws FinderException
    {
        this.initDataSource();

        final String query = 
            "SELECT ID FROM COUNTER WHERE ID=?";
        Connection        con = null;
        PreparedStatement st  = null;
        ResultSet         rs  = null;
        try {
            con = this.dataSource.getConnection();
            st  = con.prepareStatement(query);
            st.setString(1, pk);
            rs = st.executeQuery();
            if(!rs.next()) {
                throw new ObjectNotFoundException(pk);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new FinderException(ex.getMessage());
        } finally {
            try { st.close();  } catch(Exception ex) {}
            try { rs.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
        return pk;
    }

    public Collection ejbFindAllCounters()
        throws FinderException
    {
        this.initDataSource();

        final String query = 
            "SELECT ID FROM COUNTER";
        Connection con = null;
        Statement  st  = null;
        ResultSet  rs  = null;
        ArrayList  ret = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            st  = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()) {
                ret.add(rs.getString(1));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new FinderException(ex.getMessage());
        } finally {
            try { st.close();  } catch(Exception ex) {}
            try { rs.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
        return ret;
    }

    /**
      * Die Business-Methoden des Remote-Interfaces
      */

    public void inc() throws CounterOverflowException {
        if(this.counterValue < VALUE_MAX) {
            this.counterValue += 1;
        } else {
            throw new CounterOverflowException("Cannot increase above "+VALUE_MAX);
        }
    }

    public void dec() throws CounterOverflowException {
        if(this.counterValue > VALUE_MIN) {
            this.counterValue -= 1;
        } else {
            throw new CounterOverflowException("Cannot decrease below "+VALUE_MIN);
        }
    }

    public int getValue() {
        return this.counterValue;
    }

    /**
      * Die Methoden des Entity-Bean-Interfaces
      */

    public void ejbActivate() {
        this.initDataSource();
    }

    public void ejbPassivate() {
        this.dataSource = null;
    }

    public void setEntityContext(EntityContext ctx) {
        this.ctx = ctx;
    }

    public void unsetEntityContext() {
        this.ctx = null;
    }

    public void ejbLoad() {

        this.counterId = (String)this.ctx.getPrimaryKey();

        final String query = 
            "SELECT VALUE FROM COUNTER WHERE ID=?";
        Connection        con = null;
        PreparedStatement st  = null;
        ResultSet         rs  = null;
        try {
            con = this.dataSource.getConnection();
            st  = con.prepareStatement(query);
            st.setString(1, this.counterId);
            rs = st.executeQuery();
            if(rs.next()) {
                this.counterValue = rs.getInt(1);
            } else {
                throw new SQLException("id '" + this.counterId + "' not found");
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        } finally {
            try { st.close();  } catch(Exception ex) {}
            try { rs.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }

    public void ejbStore() {
        final String query = 
            "UPDATE COUNTER SET VALUE=? WHERE ID=?";
        Connection        con = null;
        PreparedStatement st  = null;
        try {
            con = this.dataSource.getConnection();
            st  = con.prepareStatement(query);
            st.setInt(1, this.counterValue);
            st.setString(2, this.counterId);
            st.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        } finally {
            try { st.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }

    public void ejbRemove() {
        final String query = 
            "DELETE FROM COUNTER WHERE ID=?";
        Connection        con = null;
        PreparedStatement st  = null;
        try {
            con = this.dataSource.getConnection();
            st  = con.prepareStatement(query);
            st.setString(1, this.counterId);
            st.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
            throw new EJBException(ex.getMessage());
        } finally {
            try { st.close();  } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }

}
