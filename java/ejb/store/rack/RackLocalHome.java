package ejb.store.rack;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.EJBLocalHome;

import ejb.store.Store;



public interface RackLocalHome extends EJBLocalHome {
    
    public RackLocal create(Integer id, String description, Integer storeId)
        throws CreateException;
    
    public RackLocal findByPrimaryKey(Integer key)
        throws FinderException;

}
