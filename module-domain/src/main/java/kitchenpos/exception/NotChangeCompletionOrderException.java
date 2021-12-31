package kitchenpos.exception;

public class NotChangeCompletionOrderException extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "계산이 완료된 주문의 상태는 변경할 수 없습니다.";

    public NotChangeCompletionOrderException() {
        super(EXCEPTION_MESSAGE);
    }
}
