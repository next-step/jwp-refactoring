package kitchenpos.order.domain.fixture;

import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import static kitchenpos.order.domain.fixture.OrderLineItemsFixture.orderLineItemsA;
import static kitchenpos.table.domain.fixture.NumberOfGuestsFixture.numberOfGuests;

public class OrdersFixture {

    public static Orders orderA() {
        return new Orders(new OrderTable(new TableGroup(), numberOfGuests(), false), orderLineItemsA());
    }
}
