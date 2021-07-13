package kitchenpos.table.dto;

import kitchenpos.table.domain.TableStatus;

public class OrderTableRequest {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private TableStatus tableStatus;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Long id, int numberOfGuests, TableStatus tableStatus) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.tableStatus = tableStatus;
    }

    public OrderTableRequest(int numberOfGuests, TableStatus tableStatus) {
        this.numberOfGuests = numberOfGuests;
        this.tableStatus = tableStatus;
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

    public TableStatus getTableStatus() {
        return tableStatus;
    }

}
