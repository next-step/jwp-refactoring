package kitchenpos.tobe.order.application;

public class OrderTableNotFoundException extends RuntimeException {

    public OrderTableNotFoundException() {
    }

    public OrderTableNotFoundException(String message) {
        super(message);
    }
}
