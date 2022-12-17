package kitchenpos.ordertable.domain;

public class OrderTableTestFixture {

    public static OrderTable create(Long id, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, numberOfGuests, empty);
    }

    public static OrderTable create(int numberOfGuests, boolean empty) {
        return OrderTable.of(null, numberOfGuests, empty);
    }
}
