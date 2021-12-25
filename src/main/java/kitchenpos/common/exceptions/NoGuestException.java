package kitchenpos.common.exceptions;

import org.springframework.http.HttpStatus;

public class NoGuestException extends CustomException {
    private static final String NO_GUEST = "손님이 없습니다.";

    public NoGuestException(final HttpStatus httpStatus) {
        super(httpStatus, NO_GUEST);
    }
}
