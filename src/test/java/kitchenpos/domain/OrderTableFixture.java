package kitchenpos.domain;

public class OrderTableFixture {
    public static OrderTable createTable(Long id, Long targetTableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, targetTableGroupId, numberOfGuests, empty);
    }

    public static OrderTable createTable(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }
}
