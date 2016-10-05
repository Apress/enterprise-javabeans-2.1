package ejb.store.rack.position;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import ejb.store.rack.RackLocal;


public interface PositionLocalHome extends EJBLocalHome {
    
    public PositionLocal create(RackLocal rack, Integer column, Integer row)
        throws CreateException;
 
    public PositionLocal findByPrimaryKey(PositionPK pk)
        throws FinderException;
    
}
