package jms.client;

import javax.jms.*;


public class MQListener implements MessageListener {
    
    public MQListener() {
    }
    
    
    public void onMessage(Message m) {
        System.err.println("MQListener:" + m);
        // ...
        // process the message
        // ...
        try {
            m.acknowledge();
        } catch(JMSException jmsex) {
            jmsex.printStackTrace();
        }
    }
    
}