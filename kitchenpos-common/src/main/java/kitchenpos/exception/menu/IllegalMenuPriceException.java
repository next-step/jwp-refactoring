package kitchenpos.menu.exception;

import org.hibernate.service.spi.ServiceException;

import java.math.BigDecimal;

/**
 * packageName : kitchenpos.menu.exception
 * fileName : IllegalMenuPriceException
 * author : haedoang
 * date : 2021-12-27
 * description :
 */
public class IllegalMenuPriceException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "가격 데이터가 유효하지 않습니다. %s";

    public IllegalMenuPriceException(BigDecimal value) {
        super(String.format(message, value));
    }
}
