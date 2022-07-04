package kitchenpos.factory.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableFixtureFactory {
    public static OrderTable createOrderTable(boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    public static OrderTable createOrderTable(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static OrderTable createOrderTable(boolean isEmpty, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static OrderTable createOrderTable(Long id, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    public static OrderTable createOrderTable(boolean isEmpty, TableGroup tableGroup) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);
        orderTable.setTableGroup(tableGroup);
        return orderTable;
    }

    public static OrderTable createOrderTable(Long id, boolean isEmpty, TableGroup tableGroup) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(isEmpty);
        orderTable.setTableGroup(tableGroup);
        return orderTable;
    }
}
