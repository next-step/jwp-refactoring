package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.empty = empty;
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable ofNumberOfGuestsAndEmpty(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public OrderTable() {
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
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public OrderTable changeEmpty(boolean empty) {
        return new OrderTable(this.id, this.tableGroupId, this.numberOfGuests, empty);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(tableGroupId,
                that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableGroupId, numberOfGuests, empty);
    }

    public OrderTable changeNumberOfGuest(int numberOfGuests) {
        return new OrderTable(this.id, this.tableGroupId, numberOfGuests, this.empty);
    }
}
