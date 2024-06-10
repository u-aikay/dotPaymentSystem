package dot.paymentproject.exceptionHandlers;

public class TransactionException extends RuntimeException{

    public TransactionException(String message){
        super(message);
    }
}
