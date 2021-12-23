package kitchenpos.tobe.fixture;

import java.util.Arrays;
import kitchenpos.tobe.orders.domain.ordertable.OrderTable;
import kitchenpos.tobe.orders.domain.ordertable.OrderTables;

public class OrderTableFixture {

    private OrderTableFixture() {
    }

    public static OrderTables ofList(final OrderTable... orderTables) {
        return new OrderTables(Arrays.asList(orderTables));
    }

    public static OrderTable of(final Long id) {
        return new OrderTable(id);
    }

    public static OrderTable of() {
        return of(null);
    }
}
