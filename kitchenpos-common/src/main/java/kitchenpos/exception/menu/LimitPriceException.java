package kitchenpos.menu.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : PriceMismatchException
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class LimitPriceException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "메뉴가격이 올바르지 않습니다.";

    public LimitPriceException() {
        super(message);
    }
}
