package kitchenpos.product.application;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
