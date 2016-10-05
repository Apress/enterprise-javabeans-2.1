package jms.client;

import javax.jms.*;
import javax.naming.*;

import ejb.util.Lookup;


/**
 * Example for how to use a Message Selector.
 *
 * @author  stefand
 * @version 
 */
public class SelectorSample extends Object {
    
    private static final String FACTORY     = "javax.jms.QueueConnectionFactory";
    private static final String SSP_FACTORY = "weblogic.jms.ServerSessionPoolFactory:mydomain";
    private static final String LISTENER    = "kapitel6.MQListener";
    private final static String SPF         = "javax.jms.ServerSessionPoolFactory";
    private static final String STOP        = "shutdown";
    

    /** Creates new SelectorSample */
    public SelectorSample() {
    }
    
    
    public void recieve(String qn)
        throws NamingException, JMSException
    {
        QueueConnectionFactory qcf = null;
        QueueConnection        qc  = null;
        QueueSession           qs  = null;
        QueueReceiver          qr  = null;
        try {
            qcf = (QueueConnectionFactory)Lookup.get(FACTORY);
            qc = qcf.createQueueConnection();
            qs = qc.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue)Lookup.get(qn);
            String selector = "AppProp in ('aProp', 'bProp')";
            qr = qs.createReceiver(queue, selector);
            qc.start();
            while(true) {
                Message m = qr.receive();
                //process message
                System.err.println(m.toString());
                m.acknowledge();
                qs.commit();
                if(m instanceof TextMessage) {
                    if(((TextMessage)m).getText().equals(STOP)) {
                        break;
                    }
                }
            }
        } finally {
            try { qr.close(); } catch(Exception ex) {}
            try { qs.close(); } catch(Exception ex) {}
            try { qc.close(); } catch(Exception ex) {}
        }
    }

}
