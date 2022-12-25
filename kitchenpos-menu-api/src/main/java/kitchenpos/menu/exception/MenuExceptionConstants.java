package kitchenpos.menu.exception;

public enum MenuExceptionConstants {

    INVALID_FORMAT_MENU("[ERROR] 요청한 메뉴는 잘못된 형식입니다."),
    INVALID_FORMAT_MENU_NAME("[ERROR] 요청한 메뉴명은 잘못된 형식입니다."),
    INVALID_FORMAT_MENU_GROUP_NAME("[ERROR] 요청한 메뉴그룹명은 잘못된 형식입니다."),
    INVALID_FORMAT_MENU_GROUP("[ERROR] 요청한 메뉴그룹명은 잘못된 형식입니다."),
    INVALID_FORMAT_MENU_QUANTITY("[ERROR] 메뉴수량은 음수가 될 수 없습니다."),
    INVALID_FORMAT_PRODUCT("[ERROR] 요청한 상품은 잘못된 형식입니다."),
    INVALID_FORMAT_PRODUCT_NAME("[ERROR] 요청한 상품명은 잘못된 형식입니다."),
    INVALID_QUANTITY("[ERROR] 유효하지 않은 수량입니다."),
    INVALID_ADD_MENU_PRICE("[ERROR] 메뉴상품 추가 시 메뉴상품의 총 금액의 합보다 메뉴의 가격이 클 수 없습니다."),
    INVALID_FORMAT_ORDER("[ERROR] 유효하지 않은 주문입니다."),;

    private final String errorMessage;

    MenuExceptionConstants(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
