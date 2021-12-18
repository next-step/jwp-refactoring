package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    private OrderTableFixture() {
    }

    public static OrderTable 주문테이블_생성(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
        return orderTable;
    }
}
