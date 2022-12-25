package kitchenpos.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class OrderTableFixture {

    public static OrderTable 주문테이블(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(tableGroup, numberOfGuests, empty);
        ReflectionTestUtils.setField(orderTable, "id", id);
        return orderTable;
    }

    public static OrderTable 빈주문테이블(Long id) {
        OrderTable orderTable = new OrderTable(null, 0, true);
        ReflectionTestUtils.setField(orderTable, "id", id);
        return orderTable;
    }

    public static OrderTable 테이블(Long id) {
        OrderTable orderTable = new OrderTable();
        ReflectionTestUtils.setField(orderTable, "id", id);
        return orderTable;
    }
}
