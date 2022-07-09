package kitchenpos.table.exception;

import static kitchenpos.table.domain.TableGroup.MIN_ORDER_TABLE_SIZE;

public class MinOrderTableSizeViolationException extends RuntimeException {
    public static final String message = String.format("단체 지정시 테이블은 최소 %d개 이상이어야 합니다.", MIN_ORDER_TABLE_SIZE);

    public MinOrderTableSizeViolationException() {
        super(message);
    }
}
