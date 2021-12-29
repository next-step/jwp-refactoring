package api.exception.table;

import api.exception.ServiceException;

/**
 * packageName : kitchenpos.table.exception
 * fileName : IllegalGuestNumberException
 * author : haedoang
 * date : 2021-12-27
 * description :
 */

public class IllegalGuestNumberException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "인원이 유효하지 않습니다. %s";

    public IllegalGuestNumberException(Integer value) {
        super(String.format(message, value));
    }
}
