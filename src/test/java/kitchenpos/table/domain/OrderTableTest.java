package kitchenpos.table.domain;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTableTest {

    public static OrderTable 주문_테이블_생성(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    public static OrderTable 주문_테이블_생성(Long orderTableId, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(orderTableId, tableGroup, numberOfGuests, empty);
    }
}
