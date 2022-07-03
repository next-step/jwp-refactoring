package kitchenpos.orderTable.domain;

import kitchenpos.tableGroup.domain.TableGroup;

public class OrderTableTest {

    public static OrderTable 주문_태이블_생성(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    public static OrderTable 주문_태이블_생성(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable 주문_태이블_생성(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }
}
