package kitchenpos.table.domain;

import java.util.Arrays;

public enum OrderTableStatus {
    EMPTY(true), USE(false);

    private boolean empty;

    OrderTableStatus(boolean empty) {
        this.empty = empty;
    }

    public static OrderTableStatus valueOf(boolean isEmpty) {
        return Arrays.stream(OrderTableStatus.values())
            .filter(s -> s.isEmpty() == isEmpty)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static boolean isEmpty(OrderTable orderTable) {
        return !EMPTY.equals(valueOf(orderTable.isEmpty()));
    }

    public boolean isEmpty() {
        return empty;
    }
}
