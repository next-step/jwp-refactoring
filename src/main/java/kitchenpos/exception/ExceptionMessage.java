package kitchenpos.exception;

public class ExceptionMessage {
    public static final String INVALID_MENU_GROUP_NAME_SIZE = "메뉴 그룹 이름은 1자 이상이어야 합니다.";
    public static final String INVALID_PRODUCT_NAME_SIZE = "상품 이름은 1자 이상이어야 합니다.";
    public static final String INVALID_MENU_NAME_SIZE = "메뉴 이름은 1자 이상이어야 합니다.";
    public static final String INVALID_MENU_PRICE = "유효하지 않은 메뉴 가격입니다.";

    private ExceptionMessage() {
    }
}
