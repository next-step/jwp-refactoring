package kitchenpos.order.exception;

public enum OrderExceptionConstants {

    ORDER_LINE_ITEMS_CANNOT_BE_EMPTY("[ERROR] 주문 상품은 비어있을 수 없습니다."),
    CANNOT_BE_CHANGED_ORDER_STATUS("[ERROR] 변경할 수 없는 주문상태 입니다."),
    ;
    private final String errorMessage;

    OrderExceptionConstants(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
