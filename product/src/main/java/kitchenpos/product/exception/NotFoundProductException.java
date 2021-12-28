package kitchenpos.product.exception;

public class NotFoundProductException extends IllegalArgumentException{
    public NotFoundProductException() {
        super();
    }

    public NotFoundProductException(String string) {
        super(string);
    }
}
