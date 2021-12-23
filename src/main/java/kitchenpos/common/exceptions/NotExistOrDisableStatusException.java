package kitchenpos.common.exceptions;

import org.springframework.http.HttpStatus;

public class NotExistOrDisableStatusException extends CustomException {
    private static final String NOT_EXIST_OR_DISABLE_STATUS = "존재하지 않거나 변경 불가능한 상태입니다.";

    public NotExistOrDisableStatusException() {
        super(HttpStatus.BAD_REQUEST, NOT_EXIST_OR_DISABLE_STATUS);
    }
}
