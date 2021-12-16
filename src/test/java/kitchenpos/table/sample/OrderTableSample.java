package kitchenpos.table.sample;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import kitchenpos.table.domain.Headcount;
import kitchenpos.table.domain.OrderTable;

public class OrderTableSample {

    public static OrderTable 채워진_다섯명_테이블() {
        OrderTable orderTable = spy(OrderTable.seated(Headcount.from(5)));
        lenient().when(orderTable.id())
            .thenReturn(1L);
        return orderTable;
    }

    public static OrderTable 빈_두명_테이블() {
        OrderTable orderTable = spy(OrderTable.empty(Headcount.from(2)));
        lenient().when(orderTable.id())
            .thenReturn(2L);
        return orderTable;
    }

    public static OrderTable 빈_세명_테이블() {
        OrderTable orderTable = spy(OrderTable.empty(Headcount.from(3)));
        lenient().when(orderTable.id())
            .thenReturn(3L);
        return orderTable;
    }
}
