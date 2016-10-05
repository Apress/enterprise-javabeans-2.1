package ejb.part;

import javax.ejb.*;
import org.apache.log4j.Category;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;


public abstract class PartBean implements EntityBean {
    
    private EntityContext theContext = null;
    //private PartDetails   theDetails;
    private TsPartDetails theDetails = null;
    
    private Category log = null;

    /** Creates new PartBean */
    public PartBean() {
        final String name = "PartBean";
        BasicConfigurator.configure();
        Category.getRoot().removeAllAppenders();
        if((log = Category.exists(name)) == null) {
            log = Category.getInstance(name);
            log.setPriority(Priority.DEBUG); //should be changed by configuration
            Layout l = new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN);
            try {
                log.addAppender(new FileAppender(l, name + ".log"));
            } catch(java.io.IOException ioex) {
                throw new IllegalStateException(ioex.getMessage());
            }
        }
        log.info("initialized instance succesfully");
    }
    
    //Die create-Methode des Home-Interface
    
    public String ejbCreate(String partNumber)
        throws CreateException
    {
        log.info("entering ejbCreate (" + partNumber + ")");
        this.setPartNumber(partNumber);
        theDetails = new TsPartDetails();
        theDetails.partNumber = partNumber;
        theDetails.partDescription = "";
        theDetails.supplierName    = "";
        theDetails.price           = 0;
        long tt = System.currentTimeMillis();
        this.setLastModified(tt);
        theDetails.updateTimestamp(theContext, tt);
        
        log.debug("Created new part with part-no:" + partNumber);
        
        log.info("leaving ejbCreate");
        return null;
    }
    
    public void ejbPostCreate(String partNumber)
        throws CreateException
    {
        log.debug("ejbPostCreate (" + partNumber + ")");
    }
    
    //Abstrakte getter-/setter-Methoden
    
    public abstract void setPartNumber(String num);
    public abstract String getPartNumber();
    public abstract void setPartDescription(String desc);
    public abstract String getPartDescription();
    public abstract void setSupplierName(String name);
    public abstract String getSupplierName();
    public abstract void setPrice(float p);
    public abstract float getPrice();
    public abstract long getLastModified();
    public abstract void setLastModified(long tt);
    
    //Die Methode des Remote-Interfaces
    
    public TsPartDetails setPartDetails(TsPartDetails pd)
        throws OutOfDateException
    {
        log.info("entering setPartDetails " + pd);
        if(theDetails.isOutDated(pd)) {
            log.warn("out of date part-details-object");
            log.info("leaving setPartDetails");
            throw new OutOfDateException();
        }
        this.setPartDescription(pd.getPartDescritption());
        this.setSupplierName(pd.getSupplierName());
        this.setPrice(pd.getPrice());
        long tt = System.currentTimeMillis();
        this.setLastModified(tt);
        theDetails = pd;
        theDetails.updateTimestamp(theContext, tt);
        log.debug("part-data updated " + theDetails);
        log.info("leaving setPartDetails");
        return theDetails;
    }
    
    //public PartDetails getPartDetails() {
    public TsPartDetails getPartDetails() {
        log.debug("getPartDetails :" + theDetails);
        return theDetails;
    }

    //Die Methoden des javax.ejb.EntityBean-Interface
    
    public void setEntityContext(EntityContext ctx) {
        log.debug("setEntityContext " + ctx);
        theContext = ctx;
    }

    public void unsetEntityContext() {
        log.debug("unsetEntityContext");
        theContext = null;  
    } 

    public void ejbRemove()
        throws RemoveException
    {
        log.debug("ejbRemove");
    }

    public void ejbActivate() {
        log.debug("ejbActivate");
    }

    public void ejbPassivate() {
        log.debug("ejbPassivate");
    }

    public void ejbLoad() {
        //theDetails = new PartDetails();
        log.info("entering ejbLoad");
        if(theDetails == null) {
            theDetails = new TsPartDetails();
        }
        theDetails.partNumber = this.getPartNumber();
        theDetails.partDescription = this.getPartDescription();
        theDetails.supplierName = this.getSupplierName();
        theDetails.price = this.getPrice();
        long tt = this.getLastModified();
        theDetails.updateTimestamp(theContext, tt);
        log.debug("data successfully loaded:" + theDetails);
        log.info("leaving ejbLoad");
    }

    public void ejbStore() {
        log.debug("ejbStore");
    }

}
