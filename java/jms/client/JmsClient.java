package jms.client;

import javax.jms.*;
import javax.naming.NamingException;

import ejb.util.Lookup;

//import weblogic.jms.ServerSessionPoolFactory;


public class JmsClient implements MessageListener {
    
    private static final String FACTORY     = "javax.jms.QueueConnectionFactory";
    private static final String SSP_FACTORY = "weblogic.jms.ServerSessionPoolFactory:mydomain";
    private static final String LISTENER    = "kapitel6.MQListener";
    private final static String SPF         = "javax.jms.ServerSessionPoolFactory";
    private static final String STOP        = "shutdown";
    
    
    public JmsClient() {
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
            qr = qs.createReceiver(queue);
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
    
    
    public synchronized void recieveAsync(String qn)
        throws NamingException, JMSException
    {
        QueueConnectionFactory qcf = null;
        QueueConnection        qc  = null;
        QueueSession           qs  = null;
        QueueReceiver          qr  = null;
        try {
            qcf = (QueueConnectionFactory)Lookup.get(FACTORY);
            qc = qcf.createQueueConnection();
            qc.start();
            qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue)Lookup.get(qn);
            qr = qs.createReceiver(queue);
            qr.setMessageListener(this);
            try { this.wait(); } catch(Exception ex) { ex.printStackTrace(); }
        } finally {
            try { qr.close(); } catch(Exception ex) {}
            try { qs.close(); } catch(Exception ex) {}
            try { qc.close(); } catch(Exception ex) {}
        }
    }
    
    
    /*public synchronized void recieveAsyncConcurrent(String qn)
        throws NamingException, JMSException
    {
        QueueConnectionFactory qcf = null;
        QueueConnection        qc  = null;
        ConnectionConsumer     cc  = null;
        try {
            qcf = (QueueConnectionFactory)Lookup.get(FACTORY);
            qc = qcf.createQueueConnection();
            qc.start();
            Queue queue = (Queue)Lookup.get(qn);
            ServerSessionPoolFactory factory = (ServerSessionPoolFactory)Lookup.get(SSP_FACTORY);
            ServerSessionPool sessionPool = 
                factory.getServerSessionPool(qc, 5, false, 
                                             Session.AUTO_ACKNOWLEDGE, 
                                             LISTENER);
            cc = qc.createConnectionConsumer(queue, "TRUE", sessionPool, 10);
            try { this.wait(); } catch(Exception ex) { ex.printStackTrace(); }
        } finally {
            try { cc.close(); } catch(Exception ex) {}
            try { qc.close(); } catch(Exception ex) {}
        }
    }*/
    
    
    public void onMessage(Message m) {
        System.err.println("async:" + m);
        // ...
        // process Message
        // ...
        try {
            m.acknowledge();
        } catch(JMSException jmsex) {
            jmsex.printStackTrace();
        }
        if(m instanceof TextMessage) {
            String s = "";
            try {
                s = ((TextMessage)m).getText();
            } catch(Exception ex) {}
            if(s.equals(STOP)) {
                synchronized(this) {
                    this.notify();
                }
            }
        }
    }
    
    
    public static void main(String[] args) {
        JmsClient c = new JmsClient();
        try {
            c.recieve("javax.jms.Jobs");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        try {
            c.recieveAsync("javax.jms.Jobs");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        /*try {
            c.recieveAsyncConcurrent("javax.jms.Jobs");
        } catch(Exception ex) {
            ex.printStackTrace();
        }*/
    }
    
}
