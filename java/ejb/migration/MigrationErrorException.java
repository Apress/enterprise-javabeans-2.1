package ejb.migration;


public class MigrationErrorException extends Exception {

    String stockId;

    public MigrationErrorException() {}

    public MigrationErrorException(String message) {
        super(message);
    }

}
