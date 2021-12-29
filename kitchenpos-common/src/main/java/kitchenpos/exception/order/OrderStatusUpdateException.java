package kitchenpos.order.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : OrderStatusUpdateException
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class OrderStatusUpdateException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "주문 상태를 변경할 수 없습니다.";

    public OrderStatusUpdateException() {
        super(message);
    }
}
