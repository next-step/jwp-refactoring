package kitchenpos.common.exceptions;

import org.springframework.http.HttpStatus;

public class OrderStatusException extends CustomException {
    private static final String NO_CHANGE_COMPLETE = "해당 주문은 이미 완료된 상태입니다.";

    public OrderStatusException(final HttpStatus httpStatus) {
        super(httpStatus, NO_CHANGE_COMPLETE);
    }
}
