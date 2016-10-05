package jms.client;

import javax.jms.*;
import javax.naming.NamingException;

import java.util.Enumeration;

import ejb.util.Lookup;


public class JmsRespondClient implements MessageListener {
    
    private static final String FACTORY     = "javax.jms.QueueConnectionFactory";
    private static final String STOP        = "shutdown";

 
    private QueueSession queueSession = null;

    
    public JmsRespondClient() {
    }
    
    
    public synchronized void recieveAsync(String qn)
        throws NamingException, JMSException
    {
        QueueConnectionFactory qcf = null;
        QueueConnection        qc  = null;
        QueueReceiver          qr  = null;
        try {
            qcf = (QueueConnectionFactory)Lookup.get(FACTORY);
            qc = qcf.createQueueConnection();
            qc.start();
            queueSession = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue)Lookup.get(qn);
            qr = queueSession.createReceiver(queue);
            qr.setMessageListener(this);
            try { this.wait(); } catch(Exception ex) { ex.printStackTrace(); }
        } finally {
            try { qr.close(); }           catch(Exception ex) {}
            try { queueSession.close(); } catch(Exception ex) {}
            try { qc.close(); }           catch(Exception ex) {}
        }
    }
    
    
    public void onMessage(Message m) {
        // ...
        // process Message
        // ...
        QueueSender qs = null;
        try {
            TextMessage tm = queueSession.createTextMessage();
            tm.setText("Reply:" + ((TextMessage)m).getText());
            Queue reply = (Queue)m.getJMSReplyTo();
            qs = queueSession.createSender(reply);
            qs.send(tm);
            m.acknowledge();
        } catch(JMSException jmsex) {
            jmsex.printStackTrace();
        } finally {
            try { qs.close(); } catch(Exception ex) {}
        }
    }
    
    
    private void reply(TextMessage in)
        throws JMSException
    {
        QueueSender qs = null;
        try {
            TextMessage tm = queueSession.createTextMessage();
            tm.setText("Reply:" + in.getText());
            Queue reply = (Queue)in.getJMSReplyTo();
            qs = queueSession.createSender(reply);
            qs.send(reply, tm);
        } finally {
            try { qs.close(); } catch(Exception ex) {}
        }
    }
    
    
    public static void main(String[] args) {
        JmsRespondClient c = new JmsRespondClient();
        try {
            c.recieveAsync("javax.jms.Jobs");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
