package kitchenpos.product.exception;

public class NoSuchProductException extends KitchenPosProductException {
    private static final String ERROR_MESSAGE = "Product가 존재하지 않습니다 (id: %d)";

    public NoSuchProductException(Long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
