package ejb.supplychain.stock;


public class ProcessingErrorException extends Exception {

    public ProcessingErrorException() {
        super();
    }

    public ProcessingErrorException(String message) {
        super(message);
    }

}
