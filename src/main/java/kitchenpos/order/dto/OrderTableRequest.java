package kitchenpos.order.dto;

public class OrderTableRequest {
    private Long id;
    private Long tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest() {}

    public OrderTableRequest(Long id, Long tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
