package kitchenpos.menu.exception;

import kitchenpos.common.exception.ServiceException;
import kitchenpos.menu.domain.MenuQuantity;

/**
 * packageName : kitchenpos.menu.exception
 * fileName : IllegalMenuQuantityException
 * author : haedoang
 * date : 2021-12-27
 * description :
 */
public class IllegalMenuQuantityException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "수량은 %d개 보다 작을 수 없습니다.";

    public IllegalMenuQuantityException() {
        super(String.format(message, MenuQuantity.MIN_QUANTITY.intValue()));
    }
}
