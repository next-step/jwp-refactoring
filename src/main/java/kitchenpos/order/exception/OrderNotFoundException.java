package kitchenpos.order.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : OrderNotFoundException
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class OrderNotFoundException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "주문이 존재하지 않습니다.";

    public OrderNotFoundException() {
        super(message);
    }
}
