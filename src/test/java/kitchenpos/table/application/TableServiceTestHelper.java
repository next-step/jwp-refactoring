package kitchenpos.table.application;

import kitchenpos.domain.OrderTable;

public class TableServiceTestHelper {
    public static OrderTable 좌석_정보(Long id, int numberOfGuests, boolean isEmpty, Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(isEmpty);
        orderTable.setTableGroupId(tableGroupId);
        return orderTable;
    }
}
