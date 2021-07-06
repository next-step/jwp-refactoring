package kitchenpos.dto.response;

import kitchenpos.domain.table.OrderTable;

import java.util.Objects;

public class OrderTableViewResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public static OrderTableViewResponse of(OrderTable orderTable) {
        Long tableGroupId = null;

        if(orderTable.getTableGroupId() != null) {
            tableGroupId = orderTable.getTableGroupId();
        }

        return new OrderTableViewResponse(
                orderTable.getId(),
                tableGroupId,
                orderTable.getNumberOfGuests().toInt(),
                orderTable.isEmpty()
        );
    }

    public OrderTableViewResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTableViewResponse that = (OrderTableViewResponse) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
