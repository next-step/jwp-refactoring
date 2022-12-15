package kitchenpos;

public enum ExceptionMessage {
    PRODUCT_PRICE_LOWER_THAN_MINIMUM("상품가격이 최소 가격보다 작습니다."),
    NOT_EXIST_MENU_GROUP("존재하지 않는 메뉴그룹입니다."),
    NOT_EXIST_PRODUCT("존재하지 않는 상품입니다."),
    QUANTITY_UNDER_ZERO("수량은 0보다 커야 합니다."),
    MENU_PRICE_LESS_PRODUCT_PRICE_SUM("메뉴의 가격은 상품의 가격 총 합보다 작거나 같아야 합니다."),
    EMPTY_MENU_PRODUCTS("메뉴상품 목록이 비어 있습니다."),
    GUEST_NUMBER_UNDER_MINIMUM("게스트 숫자를 아래 숫자 이상으로 적용해주세요 : "),
    NOT_EXIST_ORDER_TABLE("존재하지 않는 주문테이블입니다"),
    NOT_EXIST_TABLE_GROUP("존재하지 않는 테이블그룹입니다."),
    ORDER_TABLE_MINIMUM_SIZE("주문테이블 테이블 숫자가 최소 크기보다 작습니다. : "),
    NOT_EMPTY_TABLE("비어있지 않은 테이블입니다."),
    EMPTY_TABLE("비어있는 테이블입니다."),
    EXIST_TABLE_GROUP("이미 테이블그룹이 존재합니다."),
    COOKING_OR_MEAL("조리중이거나 식사중인 테이블입니다."),
    NOT_EXIST_MENU("존재하지 않는 메뉴입니다."),
    NOT_EXIST_ORDER("존재하지 않는 주문입니다."),
    ORDER_COMPLETE("완료된 상태입니다."),
    ORDER_LINE_ITEMS_EMPTY("주문항목 목록이 비어 있습니다.");



    private String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(String extraMessage) {
        return this.message + extraMessage;
    }
}
