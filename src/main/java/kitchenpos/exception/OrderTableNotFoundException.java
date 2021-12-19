package kitchenpos.exception;

public class OrderTableNotFoundException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "주문 테이블을 찾을 수 없습니다 : %d";

    public OrderTableNotFoundException(Long orderTableId) {
        super(String.format(DEFAULT_MESSAGE, orderTableId));
    }
}
