package ejb.testcases;

import ejb.logger.*;
import ejb.test.*;
import ejb.event.*;

public class TestLogger extends EJBTestCase implements EJBEventListener {
    
    private boolean ok = false;

    /** Creates new TestLogger */
    public TestLogger() {
    }
    
    
    public void prepareTest()
        throws Exception
    {
        EJBEventManager m = EJBEventManager.getInstance();
        m.addEJBEventListener(this);
    }
    
    
    public void testSuccess()
        throws Exception
    {
        Logger.log("test-logger");
        synchronized(this) {
            try {
                this.wait(10000);
            } catch(Exception ex) {
            }
        }
        if(!ok) {
            fail("no event received.");
        }
    }
    

    public void finalizeTest()
        throws Exception
    {
        EJBEventManager m = EJBEventManager.getInstance();
        m.removeEJBEventListener(this);
    }
    
    //EJBEventListener - Interface

    public void notify(EJBEvent e) {
        ok = true;
        synchronized(this) {
            this.notify();
        }
    }
    
}
