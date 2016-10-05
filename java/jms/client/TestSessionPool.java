package jms.client;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import javax.jms.Session;



public class TestSessionPool {
    
    private static int count = 0;

    public TestSessionPool(String name) {
    }
    
    
    public void testSessionPool()
        throws Exception
    {
        SessionPool pool = new SessionPool(10);
        Session[] sessions = new Session[10];
        for(int i = 0; i < 10; i++) {
            sessions[i] = new SessionImpl();
            pool.addSession(sessions[i]);
        }
        Session[] used = new Session[10];
        HashMap map = new HashMap();
        for(int i = 0; i < 10; i++) {
            used[i] = pool.getSession();
            this.doAssert("duplicate object", map.get(used[i]) == null);
            this.doAssert("pool.available != " + (10 - i - 1), pool.getAvailable() == (10 - i - 1));
            map.put(used[i], used[i]);
        }
        pool.releaseSession(used[4]);
        map.remove(used[4]);
        Session s = pool.getSession();
        this.doAssert("unexpected object  [1]", map.get(s) == null);
        this.doAssert("unexpected object  [2]", s.equals(used[4]));
        this.doAssert("pool.available != 0", pool.getAvailable() == 0);
        pool.releaseSession(s);
        this.doAssert("pool.available != 1", pool.getAvailable() == 1);
        pool.releaseSession(used[0]);
        pool.releaseSession(used[1]);
        pool.releaseSession(used[2]);
        pool.releaseSession(used[3]);
        this.doAssert("pool.available != 5", pool.getAvailable() == 5);
        s = pool.getSession();
        this.doAssert("unexpected object  [3]", s.equals(used[4]));
        
        SessionPool pool2 = new SessionPool(5);
        for(int i = 0; i < 5; i++) {
            pool2.addSession(new SessionImpl());
        }
        Helper[] helpers = new Helper[35];
        for(int i = 0; i < 35; i++) {
            helpers[i] = new Helper(pool2);
        }
        for(int i = 0; i < 35; i++) {
            helpers[i].join();
        }
    }
    
    
    static class Helper extends Thread {
        
        private SessionPool pool;
        private static Map mMap = Collections.synchronizedMap(new HashMap());
        
        public Helper(SessionPool p) {
            super("Helper#" + (count++));
            this.pool = p;
            start();
        }
        
        public void run() {
            for(int i = 0; i < 5; i++) {
                Session s = pool.getSession();
                TestSessionPool.doAssert("object is already in use",
                                         mMap.get(s) == null);
                mMap.put(s, s);
                try { sleep(500); } catch(Exception ex) {}
                mMap.remove(s);
                pool.releaseSession(s);
            }
        }
    }

    public static final void doAssert(String s, boolean b) {
        if(!b) {
            throw new IllegalStateException(s);
        }
    }
    
    /*
     * Dummy implementation of the javax.jms.Session-Interface
     */
    class SessionImpl implements Session {
        
        public SessionImpl() {
        }
        
        public void run() {
            throw new IllegalStateException("operation not supported");
        }
        public void rollback() throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public javax.jms.StreamMessage createStreamMessage() throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public void setMessageListener(final javax.jms.MessageListener p1) throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public javax.jms.TextMessage createTextMessage() throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public javax.jms.TextMessage createTextMessage(java.lang.StringBuffer sb) throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public void commit() throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public javax.jms.Message createMessage() throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public void recover() throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public javax.jms.ObjectMessage createObjectMessage(final java.io.Serializable p1) throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public javax.jms.ObjectMessage createObjectMessage() throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public javax.jms.TextMessage createTextMessage(java.lang.String p1) throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public javax.jms.MessageListener getMessageListener() throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public void close() throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public boolean getTransacted() throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public javax.jms.MapMessage createMapMessage() throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
        public javax.jms.BytesMessage createBytesMessage() throws javax.jms.JMSException {
            throw new IllegalStateException("operation not supported");
        }
    }

}
