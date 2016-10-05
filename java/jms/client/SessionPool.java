package jms.client;

import java.util.LinkedList;

import javax.jms.Session;
import javax.jms.JMSException;


public class SessionPool {

    private LinkedList sessions;
    private int        size;

    public SessionPool(int size) {
        sessions  = new LinkedList();
        this.size = size;
    }
    
    
    public synchronized void addSession(Session s) {
        if(!sessions.contains(s)) {
            sessions.addLast(s);
        } else {
            throw new IllegalArgumentException("session already in use!");
        }
    }
    
    
    public synchronized Session getSession() {
        Session ret = null;
        while(sessions.isEmpty()) {
            try {
                this.wait(10000);
            } catch(InterruptedException ex) {}
        }
        ret = (Session)sessions.removeFirst();
        return ret;
    }
    
    
    public int getAvailable() {
        return sessions.size();
    }
    
    
    public synchronized void releaseSession(Session s) {
        if(sessions.size() >= size) {
            throw new IllegalStateException("pool is exceeding initial size");
        }
        if(sessions.contains(s)) {
            throw new IllegalArgumentException("session is available");
        }
        sessions.addLast(s);
        this.notify();
    }
    
    
    public synchronized void destroy() {
        for(int i = 0; i < sessions.size(); i++) {
            try {
                ((Session)sessions.get(i)).close();
            } catch(JMSException jmsex) {
                jmsex.printStackTrace();
            }
        }
        sessions.clear();
        sessions = null;
    }

}