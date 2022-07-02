package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderStatusException extends RuntimeException {
    public static final String COMPLETE_DOES_NOT_CHANGE_MSG = "주문 상태가 완료상태이면 변경할 수 없습니다.";
    public static final String ORDER_STATUS_CAN_NOT_UNGROUP_MSG = "주문이 조리/식사 중이라 변경할 수 없습니다.";

    public OrderStatusException() {
    }

    public OrderStatusException(String msg) {
        super(msg);
    }
}
