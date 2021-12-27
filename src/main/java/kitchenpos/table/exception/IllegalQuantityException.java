package kitchenpos.table.exception;

import kitchenpos.common.exception.ServiceException;
import kitchenpos.menu.domain.MenuQuantity;

/**
 * packageName : kitchenpos.exception
 * fileName : IllegalQuantityException
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class IllegalQuantityException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "수량은 %d개 보다 작을 수 없습니다.";

    public IllegalQuantityException() {
        super(String.format(message, MenuQuantity.MIN_QUANTITY.intValue()));
    }
}
