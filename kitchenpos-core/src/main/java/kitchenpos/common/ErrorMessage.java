package kitchenpos.common;

public enum ErrorMessage {
    //menu
    MENU_REQUIRED_NAME("이름은 필수 필드입니다."),
    MENU_REQUIRED_PRICE("가격은 필수 필드입니다."),
    MENU_REQUIRED_MENU_GROUP("메뉴 그룹은 필수 필드입니다."),
    MENU_INVALID_PRICE("가격은 0보다 작거나 총 금액보다 클 수 없습니다."),
    MENU_NOT_FOUND_BY_ID("ID로 메뉴를 찾을 수 없습니다."),

    //menuGroup
    MENU_GROUP_REQUIRED_NAME("이름은 필수 필드입니다."),
    MENU_GROUP_NOT_FOUND_BY_ID("ID로 메뉴 그룹을 찾을 수 없습니다."),

    //menuProduct
    MENU_PRODUCT_REQUIRED_MENU("메뉴는 필수 필드입니다."),
    MENU_PRODUCT_REQUIRED_PRODUCT("제품은 필수 필드입니다."),
    MENU_PRODUCT_INVALID_QUANTITY("메뉴 제품의 수량은 음수일 수 없습니다."),

    //order
    ORDER_REQUIRED_ORDER_TABLE("주문 테이블은 필수 필드입니다."),
    ORDER_CANNOT_BE_CHANGED("요리 중이거나 식사 중인 테이블 상태를 변경할 수 없습니다."),
    ORDER_NOT_FOUND_BY_ID("ID로 주문 테이블을 찾을 수 없습니다."),
    ORDER_CANNOT_CHANGE_COMPLETION_ORDER("완료된 주문은 변경할 수 없습니다."),
    ORDER_TABLE_CANNOT_BE_EMPTY("빈 주문 테이블에서 주문할 수 없습니다."),
    ORDER_LINE_ITEMS_CANNOT_BE_EMPTY("주문 생성 시 빈 주문 제품을 요청할 수 없습니다."),
    ORDER_MUST_BE_GREATER_THAN_MINIMUM_SIZE("주문 메뉴 크기는 최소 크기보다 작을 수 없습니다."),

    //orderLineItem
    ORDER_LINE_ITEM_REQUIRED_ORDER("주문은 필수 필드입니다."),
    ORDER_LINE_ITEM_REQUIRED_MENU("메뉴는 필수 필드입니다."),
    ORDER_LINE_ITEM_INVALID_QUANTITY("수량은 0보다 작을 수 없습니다."),

    //orderTable
    ORDER_TABLE_NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP("비어 있지 않은 순서 테이블은 테이블 그룹에 포함될 수 없습니다."),
    ORDER_TABLE_ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP("이미 다른 테이블 그룹에 포함된 주문 테이블입니다."),
    ORDER_TABLE_NOT_FOUND_BY_ID("ID로 주문 테이블을 찾을 수 없습니다."),
    ORDER_TABLE_INVALID_NUMBER_OF_GUESTS("게스트 수는 음수일 수 없습니다."),
    ORDER_TABLE_NUMBER_OF_GUESTS_CANNOT_BE_CHANGED("빈 주문 테이블의 게스트 수를 변경할 수 없습니다."),

    //product
    PRODUCT_REQUIRED_NAME("이름은 필수 필드입니다."),
    PRODUCT_REQUIRED_PRICE("가격은 필수 필드입니다."),
    PRODUCT_INVALID_PRICE("가격은 0보다 작을 수 없습니다."),
    PRODUCT_NOT_FOUND_BY_ID("ID로 제품을 찾을 수 없습니다."),

    //tableGroup
    TABLE_GROUP_ORDER_TABLES_CANNOT_BE_EMPTY("주문 테이블은 빈 컬렉션일 수 없습니다."),
    TABLE_GROUP_MUST_BE_GREATER_THAN_MINIMUM_SIZE("주문 테이블 크기는 최소 크기보다 작을 수 없습니다."),
    TABLE_GROUP_NOT_FOUND_BY_ID("ID로 테이블 그룹을 찾을 수 없습니다."),
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
