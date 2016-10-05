package ejb.bankaccount;

import javax.ejb.*;
import org.apache.log4j.Category;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;

public abstract class BankAccountBean implements EntityBean {
    
    private EntityContext theContext;
    
    private Category log = null;
    
    public BankAccountBean() {
        final String name = "BankAccountBean";
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
        log.info("initialized instance successfully");
    }
    
    //Die create-Methode des Home-Interface
    
    public String ejbCreate(String accNo,
                            String accDescription,
                            float  initialBalance)
        throws CreateException
    {
        log.info("entering ejbCreate accNo:" + accNo);
        setAccountNumber(accNo);
        setAccountDescription(accDescription);
        setAccountBalance(initialBalance);
        log.debug("initialized successfully");
        log.debug( "accNo:" + accNo + " accDescription:"
                 + accDescription + " balance:" + initialBalance);
        log.info("leaving ejbCreate");
        return null;
    }
    
    public void ejbPostCreate(String accNo,
                              String accDescription,
                              float  initialBalance)
        throws CreateException
    {
        log.debug("ejbPostCreate accNo:" + accNo);
    }
    
    //Abstrakte getter-/setter-Methoden
    
    public abstract String getAccountNumber();
    public abstract void   setAccountNumber(String acn);
    
    public abstract String getAccountDescription();
    public abstract void   setAccountDescription(String acd);
    
    public abstract float  getAccountBalance();
    public abstract void   setAccountBalance(float acb);

    
    //Die Methoden des Remote-Interface
    
    public String getAccNumber() {
        log.debug("getAccNumber :" + getAccountNumber());
        return getAccountNumber();
    }
    
    public String getAccDescription() {
        log.debug("getAccDescription :" + getAccountDescription());
        return getAccountDescription();
    }
    
    public float getBalance() {
        log.debug("getBalance :" + getAccountBalance());
        return getAccountBalance();
    }
    
    public void increaseBalance(float amount) {
        log.info("entering increaseBalance");
        log.debug("old balance:" + getAccountBalance());
        float acb = getAccountBalance();
        acb += amount;
        setAccountBalance(acb);
        log.debug("new balance:" + getAccountBalance());
        log.info("leaving increaseBalance");
    }
    
    public void decreaseBalance(float amount) {
        log.info("entering decreaseBalance");
        log.debug("old balance:" + getAccountBalance());
        float acb = getAccountBalance();
        acb -= amount;
        setAccountBalance(acb);
        log.debug("new balance:" + getAccountBalance());
        log.info("leaving decreaseBalance");
    }
    
    //Die Methoden des javax.ejb.EntityBean-Interface
    
    public void setEntityContext(EntityContext ctx) {
        log.debug("setEntityContext :" + ctx);
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
        log.debug("ejbLoad");
    }

    public void ejbStore() {
        log.debug("ejbStore");
    }
}