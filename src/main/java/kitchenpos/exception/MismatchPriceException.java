package kitchenpos.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : PriceMismatchException
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class MismatchPriceException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "가격이 일치하지 않습니다.";

    public MismatchPriceException() {
        super(message);
    }
}
