package common;

public enum ErrorMessage {
    EMPTY_ORDER_TABLE_LIST("주문목록이 비어있습니다."),
    EMPTY_ORDER_TABLE("주문테이블이 비어있습니다."),
    INVALID_ORDER_TABLE_INFO("존재하지 않는 주문테이블 정보가 포함되어 있습니다."),
    INVALID_MENU_GROUP("메뉴그룹이 존재하지 않습니다."),
    INVALID_MENU_PRODUCT("메뉴삼품이 존재하지 않습니다."),
    INVALID_ORDER_LINE_ITEM("요청정보에 주문정보가 존재하지 않습니다."),
    INVALID_PRODUCT_ID("요청정보에 상품정보가 올바르지 않습니다."),
    INVALID_MENU_INFO("주문정보 중 존재하지 않는 메뉴 정보가 있습니다."),
    INVALID_CUSTOMER_NUMBER("고객 수가 올바르지 않습니다."),
    INVALID_PRICE("가격이 존재하지 않습니다."),
    COMPLETED_ORDER("이미 완료된 주문입니다."),
    NOT_COMPLETED_ORDER("주문상태가 완료되지 않은 주문이 포함되어 있습니다."),
    NOT_FOUND_MENU("%d에 해당하는 메뉴을 찾을 수 없습니다."),
    NOT_FOUND_MENU_GROUP("%d에 해당하는 메뉴그룹을 찾을 수 없습니다."),
    NOT_FOUND_TABLE_GROUP("%d에 해당하는 테이블그룹을 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT("%d에 해당하는 상품을 찾을 수 없습니다."),
    NOT_FOUND_ORDER("%d에 대한 주문을 찾을 수 없습니다."),
    NOT_FOUND_ORDER_TABLE("%d에 해당하는 주문테이블을 찾을 수가 없습니다."),
    VALIDATION_OF_GROUP("그룹을 지정하기 위해서는 테이블은 그룹이 지정되어 있지 않거나 테이블이 비어있어야 합니다."),
    VALIDATION_OF_PRICE("가격은 0보다 작을수 없습니다."),
    VALIDATION_OF_QUANTITY("수량은 0보다 작을 수 없습니다."),
    TABLE_HAVE_ONGOING_ORDER("해당 테이블에는 아직 진행중인 주문이 존재합니다."),
    ALREADY_TABLE_GROUP("테이블 그룹이 지정되어 있습니다."),
    NEED_TWO_ORDER_TABLE("그룹을 지정하기 위해서는 주문테이블이 2개 이상 필요합니다."),
    MENU_PRICE_LESS_THAN_SUM_OF_PRICE("메뉴 가격이 각 상품 가격의 합보다 작습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
