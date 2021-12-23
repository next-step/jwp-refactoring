package kitchenpos.common.exceptions;

import org.springframework.http.HttpStatus;

public class EmptyException extends CustomException {
    private static final String NO_EMPTY = "해당 값은 존재해야 합니다.";

    public EmptyException() {
        super(HttpStatus.BAD_REQUEST, NO_EMPTY);
    }
}
