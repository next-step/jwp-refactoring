package domain.order.exception;

import common.error.BusinessException;

public class NegativeNumberOfGuestsException extends BusinessException {
    public NegativeNumberOfGuestsException(long minNumberOfGuests) {
        super(String.format("인원은 %s보다 작을 수 없습니다.", minNumberOfGuests));
    }

    public NegativeNumberOfGuestsException(String message) {
        super(message);
    }
}
