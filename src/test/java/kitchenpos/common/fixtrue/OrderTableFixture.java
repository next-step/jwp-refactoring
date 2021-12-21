package kitchenpos.common.fixtrue;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    private OrderTableFixture() {

    }

    public static OrderTable of(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
