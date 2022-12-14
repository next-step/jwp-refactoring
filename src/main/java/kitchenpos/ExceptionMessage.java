package kitchenpos;

public enum ExceptionMessage {
    PRODUCT_PRICE_LOWER_THAN_MINIMUM("상품가격이 최소 가격보다 작습니다."),
    NOT_EXIST_MENU_GROUP("존재하지 않는 메뉴그룹입니다."),
    NOT_EXIST_PRODUCT("존재하지 않는 상품입니다."),
    QUANTITY_UNDER_ZERO("수량은 0보다 커야 합니다."),
    MENU_PRICE_LESS_PRODUCT_PRICE_SUM("메뉴의 가격은 상품의 가격 총 합보다 작거나 같아야 합니다."),
    EMPTY_MENU_PRODUCTS("메뉴상품 목록이 비어 있습니다.");

    private String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
