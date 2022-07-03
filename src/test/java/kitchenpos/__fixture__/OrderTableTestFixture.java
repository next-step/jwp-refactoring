package kitchenpos.__fixture__;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;

public class OrderTableTestFixture {
    public static OrderTable 주문_테이블_생성(final Long id, final Long tableGroupId, final int numberOfGuests,
                                       final boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public static List<OrderTable> 주문_테이블_리스트_생성(final OrderTable... orderTables) {
        return Arrays.asList(orderTables);
    }
}
