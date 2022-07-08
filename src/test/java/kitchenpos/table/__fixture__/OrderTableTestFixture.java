package kitchenpos.table.__fixture__;

import java.util.Arrays;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableTestFixture {
    public static OrderTable 주문_테이블_생성(final Long id, final TableGroup tableGroup, final int numberOfGuests,
                                       final boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static List<OrderTable> 주문_테이블_리스트_생성(final OrderTable... orderTables) {
        return Arrays.asList(orderTables);
    }
}
