package kitchenpos.domain;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderTable that = (OrderTable) o;

        if (numberOfGuests != that.numberOfGuests) return false;
        if (empty != that.empty) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return tableGroupId != null ? tableGroupId.equals(that.tableGroupId) : that.tableGroupId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tableGroupId != null ? tableGroupId.hashCode() : 0);
        result = 31 * result + numberOfGuests;
        result = 31 * result + (empty ? 1 : 0);
        return result;
    }
}
