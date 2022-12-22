package kitchenpos.common.error;

public enum ErrorEnum {
    PRICE_IS_NOT_NULL("가격은 비어있을 수 없습니다."),
    PRICE_UNDER_ZERO("가격은 0원 이하일 수 없습니다."),
    MENU_PRICE_OVER_TOTAL_PRICE("메뉴의 가격이 전체 메뉴 상품 가격의 합보다 클 수 없습니다."),
    GUESTS_UNDER_ZERO("손님은 0명 이하일 수 없습니다."),
    ORDER_TABLE_NOT_FOUND("주문 테이블을 찾을 수 없습니다."),
    ORDER_TABLE_IS_EMPTY("주문 테이블이 비어있습니다."),
    ORDER_LINE_ITEMS_IS_EMPTY("주문 항목이 비어있습니다."),
    ALREADY_GROUP("이미 단체그룹으로 지정되어 있습니다."),
    NOT_PAYMENT_ORDER("계산 완료된 주문이 아닙니다."),
    ORDER_COMPLETION_STATUS_NOT_CHANGE("계산이 완료된 주문은 변경할 수 없습니다."),
    NOT_EXISTS_ORDER_TABLE("존재하지 않는 주문 테이블입니다."),
    NOT_EXISTS_TABLE_GROUP("단체 그룹이 존재하지 않습니다."),
    NOT_EXISTS_ORDER_TABLE_LIST("주문 테이블 목록에 주문 테이블이 없습니다."),
    EXISTS_NOT_EMPTY_ORDER_TABLE("빈 상태가 아닌 주문테이블이 존재합니다."),
    ORDER_TABLE_TWO_OVER("주문 테이블은 2개 이상 존재해야 합니다."),
    QUANTITY_UNDER_ZERO("수량은 0개 이하일 수 없습니다."),
    NOT_NULL_OR_EMPTY_NAME("이름을 반드시 입력해야 합니다."),
    DEFAULT_ERROR("오류가 발생하였습니다"),
    CANNOT_CHANGE_ORDER("조리중이거나 식사중인 주문은 변경할 수 없습니다.");

    private final String message;

    public String message() {
        return message;
    }

    ErrorEnum(String message) {
        this.message  = message;
    }
}
