package ejb.store.item;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;


public interface ItemLocalHome extends EJBLocalHome {
    
    public ItemLocal create(Integer id)
        throws CreateException;
    
    public ItemLocal findByPrimaryKey(Integer key)
        throws FinderException;

}
