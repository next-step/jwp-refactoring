package kitchenpos.exception.table;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderTableException extends RuntimeException {
    public static final String ORDER_TABLE_IS_EMPTY_MSG = "주문 테이블은 비어있으면 안됩니다.";
    public static final String ORDER_TABLE_SIZE_OVER_TWO_MSG = "주문 테이블은 두 개 이상 있어야합니다.";
    public static final String ORDER_TALBE_ALREADY_HAS_GROUP_MSG = "해당 테이블은 이미 다른 단체에 지정되어 있습니다.";

    public OrderTableException() {
    }

    public OrderTableException(String msg) {
        super(msg);
    }
}
