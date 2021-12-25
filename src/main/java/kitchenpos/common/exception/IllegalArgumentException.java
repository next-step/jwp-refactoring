package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public class IllegalArgumentException extends BusinessException {
    private static final String DEFAULT_ERROR_MESSAGE = "유효하지 않은 입력값 입니다.";

    public IllegalArgumentException() {
        super(HttpStatus.BAD_REQUEST, DEFAULT_ERROR_MESSAGE);
    }

    public IllegalArgumentException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
