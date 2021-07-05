package kitchenpos.common.error;

public enum ErrorInfo {
    PRICE_CAN_NOT_NEGATIVE("가격은 음수가 될 수 없습니다."),
    NOT_FOUND_MENU_GROUP("메뉴 그룹을 찾을 수 없습니다."),
    TOTAL_PRICE_NOT_EQUAL_REQUEST("요청한 금액과 상품의 총 가격이 다릅니다"),
    NOT_FOUND_PRODUCT("상품을 찾을 수 없습니다."),
    QUANTITY_CAN_NOT_NEGATIVE("수량은 음수가 될 수 없습니다."),
    NOT_EQUAL_REQUEST_SIZE("요청한 상품 개수가 일치하지 않습니다."),
    ;

    private String message;

    ErrorInfo(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
