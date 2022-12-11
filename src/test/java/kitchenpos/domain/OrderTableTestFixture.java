package kitchenpos.domain;

public class OrderTableTestFixture {
    public static final OrderTable 주문테이블 = orderTable(1L, null, 2, false);

    public static OrderTable orderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable emptyTableRequest(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable changeGuestTableRequest(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
