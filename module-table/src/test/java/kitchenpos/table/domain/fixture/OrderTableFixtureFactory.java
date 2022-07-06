package kitchenpos.table.domain.fixture;

import kitchenpos.table.domain.OrderTable;
import org.springframework.test.util.ReflectionTestUtils;

public class OrderTableFixtureFactory {

    public static OrderTable createEmptyOrderTable() {
        return new OrderTable(0, true);
    }

    public static OrderTable createEmptyTableOrderWithId(Long id) {
        OrderTable orderTable = createEmptyOrderTable();
        ReflectionTestUtils.setField(orderTable, "id", id);
        return orderTable;
    }

    public static OrderTable createNotEmptyOrderTable(int numberOfGuests) {
        return new OrderTable(numberOfGuests, false);
    }
}
