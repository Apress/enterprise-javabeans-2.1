package ejb.payroll;


import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class PayrollBean implements SessionBean, TimedObject {


    private SessionContext beanCtx    = null;
    private DataSource     dataSource = null;


    public void ejbCreate()
        throws CreateException
    {
    }


    public void ejbRemove() {
    }

    public void processPayroll() {
        System.out.println("PayrollBean-processPayroll");
		//process the payroll
		//...

		//...
		//re-register payroll for next month
		registerPayroll();
    }

    private void registerPayroll() {
        System.out.println("PayrollBean-registerPayroll");
        TimerService timerService = beanCtx.getTimerService();

		//Create a new Calendar and increment one month and re-register the invocation
       	java.util.Calendar calendar = new java.util.GregorianCalendar();
       	calendar.setTime(new java.util.Date(System.currentTimeMillis()));
       	calendar.add(calendar.MONTH, 1);
       	Timer timer = timerService.createTimer(calendar.getTime(), "Monthly Payroll");

    }

	public void ejbTimeout(Timer timer) {
		processPayroll();
    }

    public void ejbPassivate() { }

    public void ejbActivate() { }

    public void setSessionContext(SessionContext ctx) {
        this.beanCtx = ctx;
    }

}
