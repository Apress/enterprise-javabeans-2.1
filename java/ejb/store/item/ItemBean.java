package ejb.store.item;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;


public abstract class ItemBean implements EntityBean {
    
    private EntityContext entityCtx;

    public ItemBean() { }
    
    ///////////////////
    //Lifcycle methods
    ///////////////////
    
    public Integer ejbCreate(Integer id)
        throws CreateException
    {
        if(id == null || id.intValue() < 0)
            throw new CreateException("id is null || id < 0!");
        
        this.setId(id);
        
        return null;
    }
    
    public void ejbPostCreate(Integer id)
        throws CreateException
    { }
    
    public void ejbLoad()  { }
    
    public void ejbStore() { }
    
    public void ejbActivate() { }
    
    public void ejbPassivate() { }
    
    public void ejbRemove()
        throws javax.ejb.RemoveException
    {  }
    
    public void setEntityContext(EntityContext entityContext) {
        this.entityCtx = entityContext;
    }

    public void unsetEntityContext() {
        this.entityCtx = null;
    }
    
    //////////////////////
    //Persistence methods
    //////////////////////
    
    public abstract void setId(Integer id);
    public abstract Integer getId();
    public abstract void setDescription(String desc);
    public abstract String getDescription();
    public abstract void setSupplier(String supplier);
    public abstract String getSupplier();
    
    //////////////////////
    //Business methods
    //////////////////////
    
    public Integer getItemId() {
        return this.getId();
    }
    
    public String getItemDescription() {
        return this.getDescription();
    }
    
    public String getItemSupplier() {
        return this.getSupplier();
    }

}
