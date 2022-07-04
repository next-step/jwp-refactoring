package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuantityException extends RuntimeException {
    public static final String INVALID_QUANTITY_MSG = "갯수는 0개 밑으로 설정할 수 없습니다.";

    public QuantityException() {
    }

    public QuantityException(String msg) {
        super(msg);
    }
}
