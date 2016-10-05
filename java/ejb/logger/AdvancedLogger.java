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

import jms.client.SessionPool;
import ejb.util.Lookup;

public class AdvancedLogger {

    public static final String FACTORY    = "DomeusConnectionFactory";
    public static final String QUEUE_NAME = "javax.jms.LoggerQueue";
    
    private static final int SIZE = 5;
    
    private static AdvancedLogger theInstance = null;
    
    private SessionPool     thePool;
    private QueueConnection theConnection;
    private Queue           theQueue;
    
    static {
        SessionPool     sp = null;
        QueueConnection qc = null;
        Queue           q  = null;
        try {
            QueueConnectionFactory qcf;
            qcf = (QueueConnectionFactory)Lookup.get(FACTORY);
            q   = (Queue)Lookup.get(QUEUE_NAME);
            qc  = qcf.createQueueConnection();
            sp  = new SessionPool(SIZE);
            for(int i = 0; i < SIZE; i++) {
                sp.addSession(qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex.getMessage());
        }
        theInstance = new AdvancedLogger(sp, qc, q);
    }
    
    private AdvancedLogger(SessionPool pool, QueueConnection con, Queue q) {
        this.thePool       = pool;
        this.theConnection = con;
        this.theQueue      = q;
    }
    
    public static AdvancedLogger getInstance() {
        return theInstance;
    }
    
    public void log(String logMessage)
        throws NamingException, JMSException
    {
        QueueSession    qs    = null;
        QueueSender     qsend = null;
        try {
            qs = (QueueSession)thePool.getSession();
            qsend = qs.createSender(theQueue);
            TextMessage tm = qs.createTextMessage();
            tm.setText(logMessage);
            qsend.send(tm);
        } finally {
            try  { qsend.close(); } catch(Exception ex) {}
            thePool.releaseSession(qs);
        }
    }
    
    public static void main(String[] args) {
        AdvancedLogger al = AdvancedLogger.getInstance();
        try {
            al.log("yo man, whats up?");
            al.log("die scheiss lizenz ist abgelaufen, herrschaft");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
