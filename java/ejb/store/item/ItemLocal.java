package ejb.store.item;

import javax.ejb.EJBLocalObject;


public interface ItemLocal extends EJBLocalObject {

    public Integer getItemId();
    
    public String getItemDescription();
    
    public String getItemSupplier();
    
}
