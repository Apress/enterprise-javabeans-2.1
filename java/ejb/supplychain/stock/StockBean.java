package ejb.supplychain.stock;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;


public abstract class StockBean implements EntityBean {

    private EntityContext ctx;

    public StockBean() { }

    public String ejbCreate(String stockId,
                            int maxVolume,
                            int aktVolume)
        throws CreateException
    {
        if(stockId == null)
            throw new CreateException("stockId may not be null!");
        if(aktVolume > maxVolume)
            throw new CreateException("aktVolume > maxVolume");
        if(aktVolume < 0)
            throw new CreateException("aktVolume < 0");

        this.setStockId(stockId);
        this.setStockVolume(aktVolume);
        this.setMaxStockVolume(maxVolume);

        return null;
    }

    public void ejbPostCreate(String stockId,
                              int maxVolume,
                              int aktVolume)
    {
    }

    public void ejbActivate() { }

    public void ejbPassivate() { }

    public void setEntityContext(EntityContext ctx) {
        this.ctx = ctx;
    }

    public void unsetEntityContext() {
        this.ctx = null;
    }

    public void ejbLoad() { }

    public void ejbStore() { }

    public void ejbRemove() { }

    abstract public String getStockId();
    abstract public void setStockId(String val);
    abstract public int getStockVolume();
    abstract public void setStockVolume(int val);
    abstract public int getMaxStockVolume();
    abstract public void setMaxStockVolume(int val);

    public void get(int amount) 
        throws ProcessingErrorException
    {
        int newStockVolume = this.getStockVolume() - amount;
        if(newStockVolume >= 0) {
            this.setStockVolume(newStockVolume);
        } else {
            throw new ProcessingErrorException("volume to small");
        }
    }
 
    public void put(int amount) 
        throws ProcessingErrorException
    {
        int newStockVolume = this.getStockVolume() + amount;
        if(newStockVolume <= this.getMaxStockVolume()) {
            this.setStockVolume(newStockVolume);
        } else {
            throw new ProcessingErrorException("overflow");
        }
    }
  
    public int getVolume() {
        return this.getStockVolume();
    }

}
