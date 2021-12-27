package kitchenpos.menu.exception;

import kitchenpos.common.exception.ServiceException;
import kitchenpos.menu.domain.MenuPrice;

/**
 * packageName : kitchenpos.menu.exception
 * fileName : IllegalMenuPriceException
 * author : haedoang
 * date : 2021-12-27
 * description :
 */
public class IllegalMenuPriceException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "가격은 %d원 보다 작을 수 없습니다.";

    public IllegalMenuPriceException() {
        super(String.format(message, MenuPrice.MIN_PRICE.intValue()));
    }
}
