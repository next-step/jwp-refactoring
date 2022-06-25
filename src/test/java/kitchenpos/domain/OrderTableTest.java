package kitchenpos.domain;

import kitchenpos.table.domain.OrderTable;

public class OrderTableTest {

    public static OrderTable 주문_테이블_생성(Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable result = new OrderTable();

        result.setTableGroupId(tableGroupId);
        result.setNumberOfGuests(numberOfGuests);
        result.setEmpty(empty);

        return result;
    }
}
