package kitchenpos.exception;

public class NoMenuProductListException extends IllegalArgumentException {

    private static final Long serialVersionUID = 540236956800849912L;
    private static final String NO_MENU_PRODUCT_LIST = "메뉴 상품 리스트가 없습니다. 입력값을 확인하세요";

    public NoMenuProductListException() {
        super(NO_MENU_PRODUCT_LIST);
    }

    public NoMenuProductListException(String message) {
        super(message);
    }
}
