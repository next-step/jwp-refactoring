package kitchenpos.table.sample;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import kitchenpos.table.domain.Headcount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableStatus;

public class OrderTableSample {

    public static OrderTable 채워진_다섯명_테이블() {
        OrderTable orderTable = spy(OrderTable.of(Headcount.from(5), TableStatus.FULL));
        lenient().when(orderTable.id())
            .thenReturn(1L);
        return orderTable;
    }

    public static OrderTable 빈_두명_테이블() {
        OrderTable orderTable = spy(OrderTable.of(Headcount.from(2), TableStatus.EMPTY));
        lenient().when(orderTable.id())
            .thenReturn(2L);
        return orderTable;
    }

    public static OrderTable 빈_세명_테이블() {
        OrderTable orderTable = spy(OrderTable.of(Headcount.from(3), TableStatus.EMPTY));
        lenient().when(orderTable.id())
            .thenReturn(3L);
        return orderTable;
    }
}
