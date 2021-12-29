package kitchenpos.exception.product;

import kitchenpos.exception.ServiceException;

import java.math.BigDecimal;

/**
 * packageName : kitchenpos.exception
 * fileName : IllegalPriceException
 * author : haedoang
 * date : 2021/12/20
 * description :
 */
public class IllegalPriceException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "가격이 유효하지 않습니다. %s";

    public IllegalPriceException(BigDecimal value) {
        super(String.format(message, value));
    }
}
