package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;

public class OrderTableRequest {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableRequest() {
    }

    public OrderTableRequest(Long id, int numberOfGuests) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableRequest(Long id, boolean empty) {
        this.id = id;
        this.empty = empty;
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableRequest(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests, empty);
    }
}
