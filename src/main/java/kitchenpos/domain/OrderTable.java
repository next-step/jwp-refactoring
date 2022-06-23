package kitchenpos.domain;

import kitchenpos.domain.common.Empty;
import kitchenpos.domain.common.NumberOfGuests;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private NumberOfGuests numberOfGuests;
    private Empty empty;

    public OrderTable() {
    }

    public OrderTable(Long id, boolean empty) {
        this.id = id;
        this.empty = new Empty(empty);
    }

    public OrderTable(Long id, Long tableGroupId, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.empty = new Empty(empty);
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new Empty(empty);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty.isTrue();
    }

    public void setEmpty(final boolean empty) {
        this.empty = new Empty(empty);
    }
}
