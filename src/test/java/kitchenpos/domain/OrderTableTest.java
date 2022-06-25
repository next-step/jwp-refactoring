package kitchenpos.domain;

import kitchenpos.table.domain.OrderTable;

public class OrderTableTest {

    public static OrderTable 주문_테이블_생성(Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }
}
