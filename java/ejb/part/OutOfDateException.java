package ejb.part;

public class OutOfDateException extends Exception {

    public OutOfDateException() {}

    public OutOfDateException(String msg) {
        super(msg);
    }
    
}

