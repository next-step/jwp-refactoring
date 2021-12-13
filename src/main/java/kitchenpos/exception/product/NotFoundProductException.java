package kitchenpos.exception.product;

public class NotFoundProductException extends IllegalArgumentException{
    public NotFoundProductException() {
        super();
    }

    public NotFoundProductException(String string) {
        super(string);
    }
}
