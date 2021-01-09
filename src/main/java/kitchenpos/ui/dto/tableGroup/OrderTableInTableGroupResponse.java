package kitchenpos.ui.dto.tableGroup;

import java.util.Objects;

public class OrderTableInTableGroupResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;
    private boolean grouped;

    public OrderTableInTableGroupResponse() {
    }

    public OrderTableInTableGroupResponse(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty, final boolean grouped) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.grouped = grouped;
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

    public boolean isGrouped() {
        return grouped;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTableInTableGroupResponse that = (OrderTableInTableGroupResponse) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && grouped == that.grouped && Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty, grouped);
    }

    @Override
    public String toString() {
        return "OrderTableInTableGroupResponse{" +
                "id=" + id +
                ", tableGroupId=" + tableGroupId +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                ", grouped=" + grouped +
                '}';
    }
}
