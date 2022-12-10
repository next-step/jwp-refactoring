package kitchenpos.domain;

public class OrderTableTestFixture {

    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(null, tableGroupId, numberOfGuests, empty);
    }
}
