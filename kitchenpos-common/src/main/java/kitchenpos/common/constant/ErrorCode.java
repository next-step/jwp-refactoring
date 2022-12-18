package kitchenpos.common.constant;

public enum ErrorCode {
    ORDER_STATUS_NOT_COMPLETE("계산 완료된 주문이 아닙니다."),
    ORDER_STATUS_COMPLETE("계산 완료된 주문입니다."),
    NUMBER_OF_GUESTS_MINIMUM("손님수는 0보다 작을 수 없습니다."),
    ORDER_TABLE_IS_EMPTY_STATUS("주문 테이블이 빈 상태입니다."),
    ALREADY_TABLE_GROUP("이미 단체그룹으로 지정되어 있습니다."),
    MENU_PRICE_SHOULD_NOT_OVER_TOTAL_PRICE("메뉴의 가격이 전체 메뉴 상품 가격의 합보다 클 수 없습니다."),
    ORDER_TABLES_IS_EMPTY("주문 테이블 목록에 주문 테이블이 없습니다."),
    NOT_EMPTY_STATUS_IN_ORDER_TABLES("빈 상태가 아닌 주문테이블이 존재합니다."),
    ORDER_TABLES_MINIMUM_IS_TWO("주문 테이블은 2개 이상 존재해야 합니다."),
    ORDER_TABLES_HAS_GROUP_TABLE("이미 단체 지정된 주문 테이블이 존재합니다."),
    ORDER_TABLE_IS_NOT_EXIST("존재하지 않는 주문테이블 입니다."),
    ORDER_IS_NOT_EXIST("주문이 존재하지 않습니다."),
    MENU_GROUP_IS_NOT_EXIST("메뉴그룹이 존재하지 않습니다."),
    MENU_IS_NOT_EXIST("메뉴가 존재하지 않습니다."),
    PRODUCT_IS_NOT_EXIST("존재하지 않는 상품입니다."),
    TABLE_GROUP_IS_NOT_EXIST("단체 그룹이 존재하지 않습니다."),
    ORDER_LINE_ITEMS_IS_EMPTY("주문 항목이 비어있습니다."),
    MENU_PRODUCT_IS_EMPTY("메뉴 상품이 비어있습니다.");

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return errorMessage;
    }
}
