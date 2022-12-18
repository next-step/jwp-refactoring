package kitchenpos.common;

public enum ErrorCode {

    INVALID_FORMAT_MENU("[ERROR] 요청한 메뉴는 잘못된 형식입니다."),
    INVALID_FORMAT_MENU_NAME("[ERROR] 요청한 메뉴명은 잘못된 형식입니다."),
    INVALID_FORMAT_MENU_GROUP_NAME("[ERROR] 요청한 메뉴그룹명은 잘못된 형식입니다."),
    INVALID_FORMAT_MENU_GROUP("[ERROR] 요청한 메뉴그룹명은 잘못된 형식입니다."),
    INVALID_FORMAT_MENU_QUANTITY("[ERROR] 메뉴수량은 음수가 될 수 없습니다."),


    INVALID_FORMAT_PRODUCT("[ERROR] 요청한 상품은 잘못된 형식입니다."),
    INVALID_FORMAT_PRODUCT_NAME("[ERROR] 요청한 상품명은 잘못된 형식입니다."),


    INVALID_FORMAT_PRICE("[ERROR] 요청한 금액은 잘못된 형식입니다."),
    INVALID_FORMAT_PRICE_IS_MINUS("[ERROR] 요청한 금액은 음수가 될 수 없습니다..");

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
