package api.exception.order;

import api.exception.ServiceException;

/**
 * packageName : kitchenpos.order.domain
 * fileName : IllegalOrderQuantityException
 * author : haedoang
 * date : 2021-12-27
 * description :
 */
public class IllegalOrderQuantityException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "수량이 유효하지 않습니다. %s";

    public IllegalOrderQuantityException(Long value) {
        super(String.format(message, value));
    }
}
