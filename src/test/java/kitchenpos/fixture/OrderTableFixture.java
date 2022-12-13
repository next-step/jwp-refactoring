package kitchenpos.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableFixture {

    public static OrderTable create(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

}
