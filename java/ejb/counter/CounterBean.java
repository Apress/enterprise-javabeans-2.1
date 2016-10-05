package ejb.counter;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;


public abstract class CounterBean implements EntityBean {

    public final static int VALUE_MAX = 100;
    public final static int VALUE_MIN = 0;

    private transient EntityContext ctx;

    //Die Create Methode des Home-Interfaces

    public String ejbCreate(String counterId, int initCounterValue)
        throws CreateException
    {
        if(counterId == null) {
            throw new CreateException("counterId must not be null!");
        }
        if(initCounterValue < VALUE_MIN || initCounterValue > VALUE_MAX) {
            throw new CreateException("initCounterValue out of range!");
        }
        this.setCounterId(counterId);
        this.setCounterValue(initCounterValue);
        return null;
    }

    public void ejbPostCreate(String accountId, int initCounterValue) {}

    //Abstrakte getter-/setter-Methoden
    public abstract void setCounterId(String id);
    public abstract String getCounterId();
    public abstract void setCounterValue(int value);
    public abstract int getCounterValue();
    //Abstrakte select-Methoden
    public abstract java.util.Collection ejbSelectAllCounterIds()
        throws FinderException;

    public void inc() throws CounterOverflowException {
        if(this.getCounterValue() < VALUE_MAX) {
            this.setCounterValue(this.getCounterValue() + 1);
        } else {
            throw new CounterOverflowException("Cannot increase above "+VALUE_MAX);
        }
    }

    public void dec() throws CounterOverflowException {
        if(this.getCounterValue() > VALUE_MIN) {
            this.setCounterValue(this.getCounterValue() - 1);
        } else {
            throw new CounterOverflowException("Cannot decrease below "+VALUE_MIN);
        }
    }

    public int getValue() {
        return this.getCounterValue();
    }

    public void ejbActivate() {}

    public void ejbPassivate() {}

    public void setEntityContext(EntityContext ctx) { this.ctx = ctx; }

    public void unsetEntityContext() { this.ctx = null; }

    public void ejbLoad() {}

    public void ejbStore() {}

    public void ejbRemove() {}

    // Home-Methoden

    public java.util.Collection ejbHomeGetAllCounterIds() {
        java.util.ArrayList al = new java.util.ArrayList();
        try {
            java.util.Collection col = this.ejbSelectAllCounterIds();
            java.util.Iterator it = col.iterator();
            while(it.hasNext()) {
                al.add(it.next());
            }
        } catch(FinderException ex) {
            ex.printStackTrace();
        }
        return al;
    }

}
