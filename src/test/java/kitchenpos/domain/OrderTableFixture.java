package kitchenpos.domain;

public class OrderTableFixture {
    private OrderTableFixture() {
    }

    public static OrderTable orderTableParam(Long id) {
        return new OrderTable(id, null, 0, false);
    }

    public static OrderTable orderTableParam(int numberOfGuests) {
        return new OrderTable(null, null, numberOfGuests, false);
    }

    public static OrderTable orderTableParam(boolean empty) {
        return new OrderTable(null, null, 0, empty);
    }

    public static OrderTable orderTableParam(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public static OrderTable savedOrderTable(Long id, boolean empty) {
        return new OrderTable(id, null, 0, empty);
    }

    public static OrderTable savedOrderTable(Long id, Long tableGroupId, boolean empty) {
        return new OrderTable(id, tableGroupId, 0, empty);
    }

    public static OrderTable savedOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }
}
