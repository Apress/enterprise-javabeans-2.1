package ejb.store;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ejb.store.item.ItemLocal;
import ejb.store.rack.RackLocal;
import ejb.store.rack.position.PositionLocal;
import ejb.store.rack.position.PositionLocalHome;
import ejb.store.rack.position.PositionPK;
import ejb.util.Lookup;


public abstract class StoreBean implements EntityBean {

    private EntityContext     entityCtx;
    private PositionLocalHome positionLocalHome;
    
    public StoreBean() {
    }
    
    ////////////////////
    //Life-cycle methods
    ////////////////////
    
    public Integer ejbCreate(Integer id, String name)
        throws CreateException
    {
        if(id == null || id.intValue() < 0) {
            throw new CreateException("id is null || id < 0!");
        }
        
        this.setId(id);
        this.setName(name);
        
        return null;
    }
    
    public void ejbPostCreate(Integer id, String name) { }
    
    public void ejbLoad()  { }
    
    public void ejbStore() { }
    
    public void ejbActivate() { }
    
    public void ejbPassivate() {
        this.positionLocalHome = null;
    }
    
    public void ejbRemove()
        throws javax.ejb.RemoveException
    {
        this.positionLocalHome = null;
    }
    
    public void setEntityContext(EntityContext entityContext) {
        this.entityCtx = entityContext;
    }

    public void unsetEntityContext() {
        this.entityCtx = null;
    }
    
    //////////////////////////////
    //abstract Persistence-Methods
    //////////////////////////////
    
    //Persistence-Methods
    public abstract void setId(Integer id);
    public abstract Integer getId();
    public abstract void setName(String name);
    public abstract String getName();
    //Relationship-Methods
    public abstract void setRacks(Collection racks);
    public abstract Collection getRacks();
    //Select-Methods
    public abstract Collection ejbSelectAllItems()
        throws FinderException;
    public abstract Collection ejbSelectPositionsForItem(Integer id)
        throws FinderException;
    
    ////////////////////
    //Business-methods
    ////////////////////
    
    public Integer getStoreId() {
        return this.getId();
    }

    public String getStoreName() {
        return this.getName();
    }
    
    public Collection getStoreRacks() {
        Collection racks = this.getRacks();
        Collection ret   = new java.util.ArrayList(racks.size());
        Iterator it = racks.iterator();
        while(it.hasNext()) {
            RackLocal rack = (RackLocal)it.next();
            ret.add(new Rack(rack.getRackId(),
                             rack.getRackDescription(),
                             rack.getRackStoreId()));
        }
        return ret;
    }
     
    public Collection getStoreRackPositions(Rack rack) {
        if(rack == null)
            throw new EJBException("pos is null!");

        Collection racks = this.getRacks();
        Collection ret   = new java.util.ArrayList(racks.size());
        Iterator it = racks.iterator();
        RackLocal theRack = null;
        while(it.hasNext()) {
            RackLocal r = (RackLocal)it.next();
            if(r.getRackId().intValue() == rack.getId().intValue()) {
                theRack = r;
                break;
            }

        }
        if(theRack == null) {
            return ret;
        }
        Collection positions = theRack.getRackPositions();
        it = positions.iterator();
        while(it.hasNext()) {
            PositionLocal p = (PositionLocal)it.next();
            ret.add(new Position(rack,
                                 p.getPositionRow(),
                                 p.getPositionColumn()));
        }
        return ret;
    }
     
    public Item getItemInPosition(Position p) {
        if(p == null)
            throw new EJBException("position is null!");

        if(this.positionLocalHome == null) {
            try {
                this.positionLocalHome =
                    (PositionLocalHome)Lookup.narrow(
                        "ejb.store.rack.position.PositionLocal",
                        PositionLocalHome.class);
            } catch(javax.naming.NamingException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        PositionLocal pos = null;
        try {
            PositionPK pk = new PositionPK(p.getRack().getId(),
                                           p.getRow(),
                                           p.getColumn());
            pos = this.positionLocalHome.findByPrimaryKey(pk);
        } catch(FinderException fex) {
            return null;
        }
        ItemLocal item = pos.getPositionItem();
        if(item == null) {
            return null;
        }

        return new Item(item.getItemId(),
                        item.getItemDescription(),
                        item.getItemSupplier());
    }

    public Collection getAllStoreItems() {
        ArrayList  al  = new ArrayList();
        Collection col = null;
        try {
            col = this.ejbSelectAllItems();
        } catch(FinderException ex) {
            ex.printStackTrace();
            return al;
        }
        Iterator it = col.iterator();

        while(it.hasNext()) {
            ItemLocal item = (ItemLocal)it.next();
            al.add(new Item(item.getItemId(),
                            item.getItemDescription(),
                            item.getItemSupplier()));
        }

        return al;
    }

    public Collection getPositionsForItem(Item item) {
        ArrayList  al  = new ArrayList();
        Collection col = null;
        try {
            col = this.ejbSelectPositionsForItem(item.getId());
        } catch(FinderException ex) {
            ex.printStackTrace();
            return al;
        }
        Iterator it = col.iterator();

        while(it.hasNext()) {
            PositionLocal pos  = (PositionLocal)it.next();
            RackLocal     rack = pos.getPositionRack();
            al.add(new Position(new Rack(rack.getRackId(),
                                         rack.getRackDescription(),
                                         rack.getRackStoreId()),
                                pos.getPositionRow(),
                                pos.getPositionColumn()));
        }

        return al;
    }
     
}
