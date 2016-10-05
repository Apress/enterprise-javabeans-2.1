package ejb.supplychain.producer;

public class UnappropriateStockException extends Exception {

    String stockId;

    public UnappropriateStockException() {}

    public UnappropriateStockException(String message, String stockId) {
        super(message);
        this.stockId = stockId;
    }

}
