package kitchenpos.table.domain.exception;

import kitchenpos.common.error.exception.BusinessException;

public class InvalidNumberOfGuestsException extends BusinessException {
    public InvalidNumberOfGuestsException(long minNumberOfGuests) {
        super(String.format("인원은 %s보다 작을 수 없습니다.", minNumberOfGuests));
    }

    public InvalidNumberOfGuestsException(String message) {
        super(message);
    }
}
