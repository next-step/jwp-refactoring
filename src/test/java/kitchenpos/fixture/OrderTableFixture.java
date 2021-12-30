package kitchenpos.fixture;

import java.util.Arrays;
import kitchenpos.orders.ordertable.domain.OrderTable;
import kitchenpos.orders.ordertable.domain.OrderTables;
import kitchenpos.orders.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.orders.ordertable.dto.OrderTableChangeNumberOfGuestsRequest;

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

    public static OrderTableChangeEmptyRequest ofChangeEmptyRequest(final boolean empty) {
        return new OrderTableChangeEmptyRequest(empty);
    }

    public static OrderTableChangeNumberOfGuestsRequest ofChangeNumberOfGuestsRequest(
        final int numberOfGuests
    ) {
        return new OrderTableChangeNumberOfGuestsRequest(numberOfGuests);
    }
}
