package kitchenpos.common.exceptions;

import org.springframework.http.HttpStatus;

public class NoGuestException extends CustomException {
    private static final String NO_GUEST = "손님이 없습니다.";

    public NoGuestException() {
        super(HttpStatus.BAD_REQUEST, NO_GUEST);
    }
}
