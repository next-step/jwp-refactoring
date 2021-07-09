package kitchenpos.tobe.table.dto;

import kitchenpos.tobe.table.domain.OrderTable;
import kitchenpos.tobe.table.domain.TableGroup;

import java.util.Objects;

public class OrderTableResponse {
    private Long id;
    private TableGroup tableGroup;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id, TableGroup tableGroup, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableResponse(OrderTable orderTable) {
        this.id = orderTable.getId();
        this.tableGroup = orderTable.getTableGroup();
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
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
        return Objects.equals(getId(), that.getId()) && Objects.equals(getTableGroup(), that.getTableGroup()) && Objects.equals(getNumberOfGuests(), that.getNumberOfGuests()) && Objects.equals(getEmpty(), that.getEmpty());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTableGroup(), getNumberOfGuests(), getEmpty());
    }
}
