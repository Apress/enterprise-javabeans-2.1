package ejb.logger;

import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.QueueSender;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import ejb.util.Lookup;

public class Logger {
    
    //public static final String FACTORY    = "java:/env/jms/DefaultConnectionFactory";
    //public static final String QUEUE_NAME = "java:/env/jms/LoggerQueue";
    public static final String FACTORY    = "javax.jms.QueueConnectionFactory";
    public static final String QUEUE_NAME = "javax.jms.LoggerQueue";
    
    private static QueueConnectionFactory queueCF  = null;
    private static Queue                  theQueue = null;
    
    static {
        try {
            queueCF  = (QueueConnectionFactory)Lookup.get(FACTORY);
            theQueue = (Queue)Lookup.get(QUEUE_NAME);
        } catch(NamingException nex) {
            nex.printStackTrace();
            throw new IllegalStateException(nex.getMessage());
        }
    }
    
    public static void log(String logMessage)
        throws NamingException, JMSException
    {
        QueueConnection qc    = null;
        QueueSession    qs    = null;
        QueueSender     qsend = null;
        try {
            qc = queueCF.createQueueConnection();
            qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            qsend = qs.createSender(theQueue);
            TextMessage tm = qs.createTextMessage();
            tm.setText(logMessage);
            qsend.send(tm);
        } finally {
            try  { qsend.close(); } catch(Exception ex) {}
            try  { qs.close(); }    catch(Exception ex) {}
            try  { qc.close(); }    catch(Exception ex) {}
        }
    }

}
