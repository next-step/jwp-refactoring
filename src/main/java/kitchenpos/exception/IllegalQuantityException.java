package kitchenpos.exception;

import kitchenpos.domain.Quantity;

/**
 * packageName : kitchenpos.exception
 * fileName : IllegalQuantityException
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class IllegalQuantityException extends RuntimeException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "수량은 %d개 보다 작을 수 없습니다.";

    public IllegalQuantityException() {
        super(String.format(message, Quantity.MIN_QUANTITY.intValue()));
    }
}
