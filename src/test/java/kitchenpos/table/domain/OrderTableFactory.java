package kitchenpos.table.domain;

public class OrderTableFactory {
    public static OrderTable create(Long id, TableGroup tableGroup, int numberOfGuest, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuest, empty);
    }
}
