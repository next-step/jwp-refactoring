package kitchenpos.common.exceptions;

import org.springframework.http.HttpStatus;

public class NotEqualsException extends CustomException {
    private static final String NOT_EQUALS = "비교 값이 일치하지 않습니다.";

    public NotEqualsException() {
        super(HttpStatus.BAD_REQUEST, NOT_EQUALS);
    }
}
