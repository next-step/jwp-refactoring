package kitchenpos.table.exception;

public class OutOfOrderTableException extends RuntimeException {

    public OutOfOrderTableException(int min) {
        super(String.format("단체 지정될 주문테이블은 %d개 이상이어야 합니다.", min));
    }
}
