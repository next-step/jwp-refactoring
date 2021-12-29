package kitchenpos.exception.table;

import kitchenpos.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : IllegalQuantityException
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class IllegalQuantityException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "수량이 유효하지 않습니다. %s";

    public IllegalQuantityException(Long value) {
        super(String.format(message, value));
    }
}
