package kitchenpos.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PriceException extends RuntimeException {
    public static final String INVALID_PRICE_MSG = "가격은 0원밑으로 설정할 수 없습니다.";

    public PriceException() {
    }

    public PriceException(String msg) {
        super(msg);
    }
}
