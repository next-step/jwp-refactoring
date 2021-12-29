package kitchenpos.order.exception;

import kitchenpos.common.exception.ServiceException;
import kitchenpos.order.domain.OrderQuantity;

/**
 * packageName : kitchenpos.order.domain
 * fileName : IllegalOrderQuantityException
 * author : haedoang
 * date : 2021-12-27
 * description :
 */
public class IllegalOrderQuantityException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "수량은 %d개 보다 작을 수 없습니다.";

    public IllegalOrderQuantityException() {
        super(String.format(message, OrderQuantity.MIN_QUANTITY.intValue()));
    }
}
