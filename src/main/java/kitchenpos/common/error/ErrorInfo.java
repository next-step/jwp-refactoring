package kitchenpos.common.error;

public enum ErrorInfo {
    PRICE_CAN_NOT_NEGATIVE("가격은 음수가 될 수 없습니다."),
    NOT_FOUND_MENU_GROUP("메뉴 그룹을 찾을 수 없습니다."),
    TOTAL_PRICE_NOT_EQUAL_REQUEST("요청한 금액과 상품의 총 가격이 다릅니다"),
    NOT_FOUND_PRODUCT("상품을 찾을 수 없습니다."),
    QUANTITY_CAN_NOT_NEGATIVE("수량은 음수가 될 수 없습니다."),
    NOT_EQUAL_REQUEST_SIZE("요청한 상품 개수가 일치하지 않습니다."),
    NOT_FOUND_MENU("메뉴를 찾을 수 없습니다."),
    NOT_FOUND_ORDER_TABLE("주문 테이블을 찾을 수 없습니다."),
    NOT_FOUND_ORDER("주문을 찾을 수 없습니다."),
    NOT_FOUND_ORDER_LINE_REQUEST("주문 라인 요청이 없습니다."),
    INVALID_ORDER_STATUS("주문 상태가 COOKING 혹은 MEAL 상태입니다."),
    INVALID_NUMBER_OF_GUESTS("고객 수가 음수입니다."),
    EXISTS_TABLE_GROUP("테이블 그룹이 비어 있지 않습니다."),
    ORDER_TABLE_IS_EMPTY("주문 테이블이 비어있습니다."),
    INVALID_REQUEST_ORDER_TABLE_SIZE("주문 테이블 개수가 유효하지 않습니다."),
    ORDER_TABLE_IS_NOT_EMPTY("주문 테이블이 비어있지 않습니다."),
    ALREADY_COMPLETE("이미 주문 상태가 완료입니다.");

    private String message;

    ErrorInfo(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
