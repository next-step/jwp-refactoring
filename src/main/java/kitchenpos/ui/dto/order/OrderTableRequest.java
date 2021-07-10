package kitchenpos.ui.dto.order;

import kitchenpos.domain.order.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableRequest {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableRequest() {
    }

    private OrderTableRequest(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableRequest of(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTableRequest(id, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTableRequest of(boolean empty) {
        return new OrderTableRequest(null, null, 0, empty);
    }

    public static OrderTableRequest of(int numberOfGuests) {
        return new OrderTableRequest(null, null, numberOfGuests, false);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
