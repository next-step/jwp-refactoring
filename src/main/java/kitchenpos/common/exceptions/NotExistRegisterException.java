package kitchenpos.common.exceptions;

import org.springframework.http.HttpStatus;

public class NotExistRegisterException extends CustomException {
    private static final String EXIST_MENU_GROUP = "요청한 내용은 이미 등록되어있습니다.";

    public NotExistRegisterException(final HttpStatus httpStatus) {
        super(httpStatus, EXIST_MENU_GROUP);
    }
}
