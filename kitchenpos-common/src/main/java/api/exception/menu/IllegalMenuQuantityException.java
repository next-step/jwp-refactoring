package api.exception.menu;

import api.exception.ServiceException;

/**
 * packageName : kitchenpos.menu.exception
 * fileName : IllegalMenuQuantityException
 * author : haedoang
 * date : 2021-12-27
 * description :
 */
public class IllegalMenuQuantityException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "수량이 유효하지 않습니다. %s";

    public IllegalMenuQuantityException(Long value) {
        super(String.format(message, value));
    }
}
