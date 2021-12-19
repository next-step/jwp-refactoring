package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    private OrderTableFixture() {
    }

    public static OrderTable 주문테이블_생성(Long id, Long tableGroupId, boolean empty,
        int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
