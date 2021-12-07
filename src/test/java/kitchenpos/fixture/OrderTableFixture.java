package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 주문_테이블(int numberOfGuests, boolean empty) {
        return 주문_테이블(numberOfGuests, null, empty);
    }

    public static OrderTable 주문_테이블(int numberOfGuests, Long tableGroupId, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        orderTable.setTableGroupId(tableGroupId);
        return orderTable;
    }
}
