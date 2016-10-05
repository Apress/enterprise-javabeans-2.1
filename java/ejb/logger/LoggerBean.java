package ejb.logger;

import javax.ejb.*;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import ejb.event.EJBEvent;
import ejb.event.EJBEventHelper;
import ejb.util.Lookup;


public class LoggerBean implements MessageDrivenBean, MessageListener {
    
    private static final String DS = "java:/comp/env/ds-name";
    private static final String ST = "insert into logs(tst, msg) values(?, ?)";
    
    private MessageDrivenContext mCtx       = null;
    
    private transient DataSource dataSource = null;
    private transient EJBEventHelper helper = null;
    
    public LoggerBean() {}
    
    
    public void setMessageDrivenContext(MessageDrivenContext ctx) {
        mCtx = ctx;
    }
    
    
    public void ejbCreate() {
        try {
            helper = new EJBEventHelper();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void ejbRemove() {
        dataSource = null;
        helper     = null;
    }
    
    
    public void onMessage(Message message) {
        try {
            initDataSource();
        } catch(NamingException nex) {
            //Loggen des Fehlers
            mCtx.setRollbackOnly();
            return;
        }
        String msg = null;
        try {
            msg = ((TextMessage)message).getText();
        } catch(ClassCastException ccex) {
            //Loggen des Fehlers
            mCtx.setRollbackOnly();
            return;
        } catch(JMSException jmsex) {
            //Loggen des Fehlers
            mCtx.setRollbackOnly();
            return;
        }
        try {
            logMessage(msg);
        } catch(SQLException sqlex) {
            //Loggen des Fehlers
            mCtx.setRollbackOnly();
            return;
        }
        try {
            helper.fireEvent(new EJBEvent(0));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    private void logMessage(String msg)
        throws SQLException
    {
        Connection        con = dataSource.getConnection();
        PreparedStatement pst = null;
        String            mm  = (msg == null) ? "" : msg;
        try {
            pst = con.prepareStatement(ST);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            pst.setTimestamp(1, ts);
            pst.setString(2, mm);
            pst.execute();
        } finally {
            try { pst.close(); } catch(Exception ex) {}
            try { con.close(); } catch(Exception ex) {}
        }
    }
    
    
    private void initDataSource()
        throws NamingException
    {
        if(dataSource != null) {
            return;
        }
        String dsname = (String)Lookup.narrow(DS, String.class);
        dataSource = (DataSource)Lookup.narrow(dsname, DataSource.class);
    }
    
}
