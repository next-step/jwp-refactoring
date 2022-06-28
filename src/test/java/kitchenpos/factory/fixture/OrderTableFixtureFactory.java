package kitchenpos.factory.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtureFactory {
    public static OrderTable createOrderTable(Long id, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    public static OrderTable createOrderTable(Long id, boolean isEmpty, Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(isEmpty);
        orderTable.setTableGroupId(tableGroupId);
        return orderTable;
    }
}
