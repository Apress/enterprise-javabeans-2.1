package ejb.store.rack.position;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import ejb.store.item.ItemLocal;
import ejb.store.rack.RackLocal;


public abstract class PositionBean implements EntityBean {
    
    private EntityContext entityCtx;

    public PositionBean() { }
    
    ///////////////////
    //Licecycle methods
    ///////////////////
    
    public PositionPK ejbCreate(RackLocal rack, Integer column, Integer row)
        throws CreateException
    {
        if(rack == null)
            throw new CreateException("rack is null!");
        if(column == null || column.intValue() < 0)
            throw new CreateException("column is null || column < 0!");
        if(row == null || row.intValue() < 0)
            throw new CreateException("row is null || row < 0!");
        
        this.setRackId(rack.getRackId());
        this.setColumn(column);
        this.setRow(row);
        
        return null;
    }
    
    public void ejbPostCreate(RackLocal rack, Integer column, Integer row)
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
    
    //Persistence Methods
    public abstract void setRackId(Integer rackid);
    public abstract Integer getRackId();
    public abstract void setRow(Integer row);
    public abstract Integer getRow();
    public abstract void setColumn(Integer column);
    public abstract Integer getColumn();
    public abstract void setItemId(Integer itemId);
    public abstract Integer getItemId();
    public abstract void setQuantity(Long quantity);
    public abstract Long getQuantity();
    //Relationship-methods
    public abstract void setRack(RackLocal rack);
    public abstract RackLocal getRack();
    public abstract void setItem(ItemLocal item);
    public abstract ItemLocal getItem();
    
    //////////////////////
    //Business methods
    //////////////////////
    
    public Integer getPositionRow() {
        return this.getRow();
    }
    
    public Integer getPositionColumn() {
        return this.getColumn();
    }
    
    public RackLocal getPositionRack() {
        return this.getRack();
    }
    
    public ItemLocal getPositionItem() {
        return this.getItem();
    }

    public Long getItemQuantity() {
        return this.getQuantity();
    }
    
}
