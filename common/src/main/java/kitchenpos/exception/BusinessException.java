package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private ErrorCode code;

    public BusinessException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return code.getStatus();
    }
}
