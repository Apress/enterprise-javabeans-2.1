package ejb.test;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public abstract class EJBTestCase {

    private Properties theProps;
    private Context    theContext;

    public EJBTestCase() {
    }

    void setProperties(Properties p)
        throws NamingException
    {
        if(p == null) {
            throw new IllegalArgumentException("null is not allowed!");
        }
        theProps   = p;
        theContext = new InitialContext(theProps);
    }
    
    public Object lookup(String name)
        throws NamingException
    {
        this.assertNotNull("name", name);
        return theContext.lookup(name);
    }
 
    public Object narrow(String name, Class c)
        throws NamingException
    {
        this.assertNotNull("name", name);
        this.assertNotNull("class", c);
        Object o = theContext.lookup(name);
        return javax.rmi.PortableRemoteObject.narrow(o, c);
    }
 
    public void fail(String msg) {
        throw new TestFailException(msg);
    }
    
    public void assertEquals(Object obj1, Object obj2) {
        assertEquals("values do not match", obj1, obj2);
    }
    
    public void assertEquals(float f1, float f2) {
        assertEquals("values do not match", f1, f2);
    }
    
    public void assertEquals(int i1, int i2) {
        assertEquals("values do not match", i1, i2);
    }
    
    public void assertEquals(String msg, Object obj1, Object obj2) {
        if(!obj1.equals(obj2)) {
            throw new AssertionException(msg + ": " +obj1 + " != " + obj2);
        }
    }
    
    public void assertEquals(String msg, float f1, float f2) {
        if(f1 != f2) {
            throw new AssertionException(msg + ": " +f1 + " != " + f2);
        }
    }
    
    public void assertEquals(String msg, int i1, int i2) {
        if(i1 != i2) {
            throw new AssertionException(msg + ": " +i1 + " != " + i2);
        }
    }
    
    public void assertNotNull(String name, Object obj) {
        if(obj == null) {
            throw new AssertionException(name + " is null");
        }
    }

    public void assertTrue(String name, boolean expr) {
        if(!expr) {
            throw new AssertionException(name);
        }
    }

    public Properties getProperties() {
        return theProps;
    }

    public abstract void prepareTest()
        throws Exception;

    public abstract void finalizeTest()
        throws Exception;

}
