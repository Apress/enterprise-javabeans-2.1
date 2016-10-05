package ejb.util;

import javax.jms.*;
import javax.naming.NamingException;
import java.io.Serializable;


public class JmsHelper {
    
    private static final String FACTORY = "javax.jms.QueueConnectionFactory";
    
    private static QueueConnection qc = null;
    
    
    private JmsHelper() {
    }
    
    
    public static Message sendReceiveTextMessage(String qn, String msg)
        throws NamingException, JMSException
    {
        QueueConnection qc    = null;
        QueueSession    qs    = null;
        QueueSender     qsend = null;
        QueueRequestor  qr    = null;
        Message         ret   = null;
        try {
            QueueConnectionFactory qcf = null;
            qcf = (QueueConnectionFactory)Lookup.get(FACTORY);
            qc = qcf.createQueueConnection();
            qc.start();
            qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue)Lookup.get(qn);
            qsend = qs.createSender(queue);
            TextMessage m = null;
            //m = qs.createTextMessage(msg);
            m = qs.createTextMessage();
            m.setText(msg);
            qr = new QueueRequestor(qs, queue);
            ret = qr.request(m);
            //...
            //process answer
            //...
        } finally {
            try  { qr.close(); }    catch(Exception ex) {}
            try  { qsend.close(); } catch(Exception ex) {}
            try  { qs.close(); }    catch(Exception ex) {}
            try  { qc.close(); }    catch(Exception ex) {}
        }
        return ret;
    }
    
    
    public static void sendTextMessage(String qn, String msg)
        throws NamingException, JMSException
    {
        sendMessage(qn, msg);
    }
    
    
    public static void sendObjectMessage(String qn, Serializable obj)
        throws NamingException, JMSException
    {
        sendMessage(qn, obj);
    }

    
    private static void sendMessage(String qn, Serializable obj)
        throws NamingException, JMSException
    {
        QueueConnection qc    = null;
        QueueSession    qs    = null;
        QueueSender     qsend = null;
        try {
            QueueConnectionFactory qcf = null;
            qcf = (QueueConnectionFactory)Lookup.get(FACTORY);
            qc = qcf.createQueueConnection();
            qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue)Lookup.get(qn);
            qsend = qs.createSender(queue);
            Message m = null;
            if(obj instanceof java.lang.String) {
                //m = qs.createTextMessage((String)obj);
                m = qs.createTextMessage();
                ((TextMessage)m).setText((String)obj);
            } else {
                m = qs.createObjectMessage(obj);
            }
            qsend.send(m);
        } finally {
            try  { qsend.close(); } catch(Exception ex) {}
            try  { qs.close(); }    catch(Exception ex) {}
            try  { qc.close(); }    catch(Exception ex) {}
        }
    }
    
    
    public static void main(String[] args) {
        try {
            sendReceiveTextMessage(args[0], args[1]);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
