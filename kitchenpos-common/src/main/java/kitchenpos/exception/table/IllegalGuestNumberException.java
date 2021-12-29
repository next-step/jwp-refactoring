package kitchenpos.table.exception;

import kitchenpos.common.exception.ServiceException;
import kitchenpos.table.domain.NumberOfGuests;

/**
 * packageName : kitchenpos.table.exception
 * fileName : IllegalGuestNumberException
 * author : haedoang
 * date : 2021-12-27
 * description :
 */

public class IllegalGuestNumberException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "인원은 %d명 보다 작을 수 없습니다.";

    public IllegalGuestNumberException() {
        super(String.format(message, NumberOfGuests.MIN_GUESTS));
    }
}
