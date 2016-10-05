package ejb.testcases;

import ejb.counter.*;
import ejb.test.*;
import javax.ejb.*;

public class TestCounterBean extends EJBTestCase {
    
    CounterHome counterHome;
    Counter counter;
    Counter counter1;
    Counter counter2;
    Counter counter3;


    public TestCounterBean() {
        super();
    }


    public void prepareTest()
        throws Exception
    {
        counterHome = (CounterHome)narrow("Counter", CounterHome.class);
    }


    public void testCreateAndFind()
        throws Exception
    {
        String id = Long.toString(System.currentTimeMillis())
                  + "-" + System.identityHashCode(counterHome);
        try {
            counterHome.create(null, 1);
            fail("expected CreateException, id is null");
        } catch(CreateException cex) {
            //as expected
        }
        try {
            counterHome.create(id, CounterBean.VALUE_MIN - 1);
            fail("expected CreateException, initial value is too low");
        } catch(CreateException cex) {
            //as expected
        }
        try {
            counterHome.create(id, CounterBean.VALUE_MAX + 1);
            fail("expected CreateException, initial value is too high");
        } catch(CreateException cex) {
            //as expected
        }
        counter = counterHome.create(id, 0);

        Counter cc = counterHome.findByPrimaryKey(id);
        this.assertNotNull("Counter-Instance", cc);

        java.util.Collection col = counterHome.getAllCounterIds();
        this.assertNotNull("result of getAllCounterIds", col);
        this.assertTrue("result of getAllCounterIds", col.size() >= 1);
    }


    public void testIncrement()
        throws Exception
    {
        String id = Long.toString(System.currentTimeMillis())
                  + "-" + System.identityHashCode(counterHome);
        counter1 = counterHome.create(id, CounterBean.VALUE_MIN);

        int initVal = counter1.getValue();
        assertEquals("counter value after creation", CounterBean.VALUE_MIN, initVal);

        counter1.inc();

        int val = counter1.getValue();
        assertEquals("counter value after increment", initVal + 1, val);

        do {
            counter1.inc();
        } while(counter1.getValue() < CounterBean.VALUE_MAX);

        try {
            counter1.inc();
        } catch(CounterOverflowException ex) {
            //as expected
        }
    }


    public void testDecrement()
        throws Exception
    {
        String id = Long.toString(System.currentTimeMillis())
                  + "-" + System.identityHashCode(counterHome);
        counter2 = counterHome.create(id, CounterBean.VALUE_MAX);

        int initVal = counter2.getValue();
        assertEquals("counter value after creation", CounterBean.VALUE_MAX, initVal);

        counter2.dec();

        int val = counter2.getValue();
        assertEquals("counter value after increment", initVal - 1, val);

        do {
            counter2.dec();
        } while(counter2.getValue() > CounterBean.VALUE_MIN);

        try {
            counter2.dec();
        } catch(CounterOverflowException ex) {
            //as expected
        }
    }


    public void testDelete()
        throws Exception
    {
        String id = Long.toString(System.currentTimeMillis())
                  + "-" + System.identityHashCode(counterHome);
        counter3 = counterHome.create(id, CounterBean.VALUE_MIN);

        counter3.remove();

        try {
            counterHome.findByPrimaryKey(id);
        } catch(FinderException fex) {
            //as expected
        }
    }


    public void finalizeTest()
        throws Exception
    {
        counter.remove();
        counter1.remove();
        counter2.remove();
    }

}
