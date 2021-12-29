package kitchenpos.product.exception;

import kitchenpos.common.exception.ServiceException;
import kitchenpos.product.domain.Price;

/**
 * packageName : kitchenpos.exception
 * fileName : IllegalPriceException
 * author : haedoang
 * date : 2021/12/20
 * description :
 */
public class IllegalPriceException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "가격은 %d원 보다 작을 수 없습니다.";

    public IllegalPriceException() {
        super(String.format(message, Price.MIN_PRICE.intValue()));
    }
}
