package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.Objects;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableResponse(OrderTable orderTable) {
        this.id = orderTable.getId();
        this.tableGroupId = orderTable.getTableGroupId();
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderTableResponse)) return false;
        OrderTableResponse that = (OrderTableResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getTableGroupId(), that.getTableGroupId()) && Objects.equals(getNumberOfGuests(), that.getNumberOfGuests()) && Objects.equals(getEmpty(), that.getEmpty());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTableGroupId(), getNumberOfGuests(), getEmpty());
    }
}
