package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BusinessException {
    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
    }

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
