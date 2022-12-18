package kitchenpos.order.exception;

public enum OrderExceptionType {

    COMPLETE_ORDER_STATUS("계산이 완료된 주문은 상태를 변경할 수 없습니다"),
    BEFORE_COMPLETE_ORDER_STATUS("계산이 끝나지 않은 주문은 상태를 변경할 수 없습니다"),
    EMPTY_ORDER_TABLE("주문테이블이 존재하지 않습니다");

    public String message;

    OrderExceptionType(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
