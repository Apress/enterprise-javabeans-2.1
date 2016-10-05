package ejb.store.client;


public final class Assert {


    private Assert() {}


    public static void notNull(String name, Object obj) {
        if(obj == null) {
            throw new AssertionException(name + " must not be null!");
        }
    }


    public static final class AssertionException extends RuntimeException {

        public AssertionException() {
            super();
        }

        public AssertionException(String msg) {
            super(msg);
        }

    }

}
