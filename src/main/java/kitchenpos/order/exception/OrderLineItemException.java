package kitchenpos.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderLineItemException extends RuntimeException{
    public static final String ORDER_LINE_ITEM_SIZE_INVALID = "유효하지 않은 주문 상품이 있습니다";

    public OrderLineItemException() {
    }

    public OrderLineItemException(String msg) {
        super(msg);
    }
}
