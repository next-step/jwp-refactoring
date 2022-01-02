package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BusinessException {
    private static final String DEFAULT_ERROR_MESSAGE = "해당 항목을 찾을 수 없습니다.";

    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, DEFAULT_ERROR_MESSAGE);
    }

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
