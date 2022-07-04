package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;

public class OrderTableResponse {
    private long id;
    private int numberOfGuests;
    private boolean empty;

    private Long tableGroupId;

    public OrderTableResponse() {

    }

    private OrderTableResponse(long id, int numberOfGuests, boolean empty, Long tableGroupId) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.tableGroupId = tableGroupId;
    }

    public static OrderTableResponse formOrderTable(OrderTable saveTable) {
        return new OrderTableResponse(saveTable.getId(), saveTable.getNumberOfGuests(), saveTable.isEmpty(),
                saveTable.getTableGroupId());
    }

    public static List<OrderTableResponse> of(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::formOrderTable)
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
