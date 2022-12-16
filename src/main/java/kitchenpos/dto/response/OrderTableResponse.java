package kitchenpos.dto.response;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableResponse {
    private Long id;
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse() {
    }

    private OrderTableResponse(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTableResponse(id, tableGroup, numberOfGuests, empty);
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        return OrderTableResponse.of(
                orderTable.getId(),
                orderTable.getTableGroup(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
