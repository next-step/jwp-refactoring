package kitchenpos.advice.exception;

public class MenuException extends BusinessException {

    public MenuException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }

    public MenuException(String message, long menuPrice, long sumPrice) {
        super(String.format(message + "메뉴가격 : %d, 상품 총가격 : %d", menuPrice, sumPrice));
    }
}
