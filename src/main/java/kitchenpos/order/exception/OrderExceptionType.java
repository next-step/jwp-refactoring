package kitchenpos.order.exception;

public enum OrderExceptionType {
    NOT_EXIST_ORDER_ITEM("상품을 주문하지 않았습니다."),
    NOT_MATCH_MENU_SIZE("주문 상품이 존재하지 않습니다."),
    NOT_FOUND_ORDER_NO("조회 되는 주문번호가 없습니다.");


    private final String message;

    OrderExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
