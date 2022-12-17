package kitchenpos.common.error;

public enum ErrorEnum {
    PRICE_IS_NOT_NULL("가격은 비어있을 수 없습니다."),
    PRICE_UNDER_ZERO("가격은 0원 이하일 수 없습니다."),
    MENU_PRICE_OVER_TOTAL_PRICE("메뉴의 가격이 전체 메뉴 상품 가격의 합보다 클 수 없습니다."),
    GUESTS_UNDER_ZERO("손님은 0명 이하일 수 없습니다."),
    ORDER_TABLE_NOT_EMPTY("주문 테이블이 빈 상태가 아닙니다."),
    ALREADY_GROUP("이미 단체그룹으로 지정되어 있습니다."),
    NOT_PAYMENT_ORDER("계산 완료된 주문이 아닙니다."),
    DEFAULT_ERROR("오류가 발생하였습니다");

    private final String message;

    public String message() {
        return message;
    }

    ErrorEnum(String message) {
        this.message  = message;
    }
}
