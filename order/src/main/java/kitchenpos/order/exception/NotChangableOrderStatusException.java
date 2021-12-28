package kitchenpos.order.exception;

public class NotChangableOrderStatusException extends IllegalArgumentException{
    public NotChangableOrderStatusException() {
        super();
    }

    public NotChangableOrderStatusException(String string) {
        super(string);
    }
    
}
