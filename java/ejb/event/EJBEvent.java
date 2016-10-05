package ejb.event;

import javax.ejb.Handle;

public class EJBEvent implements java.io.Serializable {
    
    private int    eventType;
    private Handle eventSource;

    public EJBEvent(int type) {
        this(type, null);
    }
    
    public EJBEvent(int type, Handle source) {
        eventSource = source;
        eventType   = type;
    }
    
    public Handle getEventSource() {
        return eventSource;
    }
    
    public int getEventType() {
        return eventType;
    }

}