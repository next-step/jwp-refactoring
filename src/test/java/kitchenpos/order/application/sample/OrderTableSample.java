package kitchenpos.order.application.sample;

import kitchenpos.table.domain.Headcount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableStatus;

public class OrderTableSample {

    public static OrderTable 채워진_다섯명_테이블() {
        return OrderTable.of(Headcount.from(5), TableStatus.FULL);
    }

    public static OrderTable 빈_두명_테이블() {
        return OrderTable.of(Headcount.from(2), TableStatus.EMPTY);
    }

    public static OrderTable 빈_세명_테이블() {
        return OrderTable.of(Headcount.from(3), TableStatus.EMPTY);
    }
}
