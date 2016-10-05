package ejb.store.rack.position;

import javax.ejb.EJBLocalObject;

import ejb.store.item.ItemLocal;
import ejb.store.rack.RackLocal;


public interface PositionLocal extends EJBLocalObject {
    
    public RackLocal getPositionRack();
    
    public Integer getPositionRow();
    
    public Integer getPositionColumn();
    
    public ItemLocal getPositionItem();

    public Long getItemQuantity();
    
}
