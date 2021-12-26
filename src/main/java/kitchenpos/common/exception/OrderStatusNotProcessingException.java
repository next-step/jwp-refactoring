package kitchenpos.common.exception;

public class OrderStatusNotProcessingException extends RuntimeException {
    public static final String ORDER_STATUS_NOT_PROCESSING_MESSAGE = "식사 중이나 요리 중 상태가 아닙니다.";

    public OrderStatusNotProcessingException() {
        super(ORDER_STATUS_NOT_PROCESSING_MESSAGE);
    }
}
