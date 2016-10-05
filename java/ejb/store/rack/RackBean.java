package ejb.store.rack;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import java.util.Collection;

import ejb.store.Store;


public abstract class RackBean implements EntityBean {
    
    private EntityContext entityCtx;

    public RackBean() { }
    
    /////////////////////
    //Lice-cycle methods
    /////////////////////
    
    public Integer ejbCreate(Integer id, String description, Integer storeId)
        throws CreateException
    {
        if(id == null || id.intValue() < 0)
            throw new CreateException("id is null || id < 0!");
        if(storeId == null || storeId.intValue() < 0)
            throw new CreateException("storeId is null || storeId < 0!");
        
        this.setId(id);
        this.setDescription(description);
        this.setStoreId(storeId);
        
        return null;
    }
    
    public void ejbPostCreate(Integer id, String description, Integer storeId)
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
    
    ///////////////////////////////
    //Abstract Persistence-methods
    ///////////////////////////////
    
    //Persistence Methods
    public abstract void setId(Integer id);
    public abstract Integer getId();
    public abstract void setDescription(String desc);
    public abstract String getDescription();
    public abstract void setStoreId(Integer id);
    public abstract Integer getStoreId();
    //Relationship Methods
    public abstract Collection getPositions();
    public abstract void setPositions(Collection col);
    
    ///////////////////
    //Business methods
    ///////////////////
    
    public Integer getRackId() {
        return this.getId();
    }
    
    public String getRackDescription() {
        return this.getDescription();
    }
    
    public Collection getRackPositions() {
        return this.getPositions();
    }

    public Integer getRackStoreId() {
        return this.getStoreId();
    }
    
}
