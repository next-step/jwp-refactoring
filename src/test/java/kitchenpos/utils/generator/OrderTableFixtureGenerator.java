package kitchenpos.utils.generator;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.OrderTable;

public class OrderTableFixtureGenerator {

    private static int NUMBER_OF_GUESTS = 0;
    private static boolean EMPTY = false;
    private static int COUNTER = 0;

    public static OrderTable generateOrderTable() {
        COUNTER++;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(NUMBER_OF_GUESTS + COUNTER);
        orderTable.setEmpty(EMPTY);
        return orderTable;
    }

    public static List<OrderTable> generateOrderTables(int count) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            orderTables.add(generateOrderTable());
        }
        return orderTables;
    }
}
