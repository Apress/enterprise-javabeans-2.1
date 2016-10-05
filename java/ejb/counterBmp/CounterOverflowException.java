package ejb.counterBmp;


public class CounterOverflowException extends Exception {


    public CounterOverflowException() {
        super();
    }


    public CounterOverflowException(String message) {
        super(message);
    }

}
