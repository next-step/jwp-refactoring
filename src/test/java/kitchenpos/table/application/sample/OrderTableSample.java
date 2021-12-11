package kitchenpos.table.application.sample;

import kitchenpos.table.domain.Headcount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableStatus;

public class OrderTableSample {

    public static OrderTable 채워진_다섯명_테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(Headcount.from(5));
        orderTable.setStatus(TableStatus.FULL);
        return orderTable;
    }

    public static OrderTable 빈_두명_테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(2L);
        orderTable.setNumberOfGuests(Headcount.from(2));
        orderTable.setStatus(TableStatus.EMPTY);
        return orderTable;
    }

    public static OrderTable 빈_세명_테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(3L);
        orderTable.setNumberOfGuests(Headcount.from(3));
        orderTable.setStatus(TableStatus.EMPTY);
        return orderTable;
    }
}
