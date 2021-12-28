package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;

public class TableResponse {

    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;

    public TableResponse(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableResponse from(OrderTable orderTable) {
        return new TableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<TableResponse> from(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(TableResponse::from)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
