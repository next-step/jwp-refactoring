package kitchenpos.table.exception;

import kitchenpos.order.exception.CustomException;

public class MinimumOrderTableNumberException extends CustomException {
    public static final String MINIMUM_ORDER_TABLE_NUMBER_MESSAGE = "최소 주문 테이블 숫자가 필요합니다.";

    public MinimumOrderTableNumberException() {
        super(MINIMUM_ORDER_TABLE_NUMBER_MESSAGE);
    }
}
