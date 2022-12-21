package kitchenpos.table.domain;

public class OrderTableFactory {
    public static OrderTable create(Long id, Long tableGroupId, int numberOfGuest, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuest, empty);
    }
}
