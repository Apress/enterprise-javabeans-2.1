package ejb.store.rack;

import javax.ejb.EJBLocalObject;

import java.util.Collection;


public interface RackLocal extends EJBLocalObject {
    
    public Integer getRackId();
    
    public String getRackDescription();
    
    public Integer getRackStoreId();
    
    public Collection getRackPositions();

}
