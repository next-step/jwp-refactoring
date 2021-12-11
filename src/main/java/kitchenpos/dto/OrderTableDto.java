package kitchenpos.dto;

import java.util.Objects;

import kitchenpos.domain.table.OrderTable;

public class OrderTableDto {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableDto() {
    }

    private OrderTableDto(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableDto of(int numberOfGuests) {
        if (numberOfGuests > 0) {
            return new OrderTableDto(null, null, numberOfGuests, false);
        }

        return new OrderTableDto(null, null, numberOfGuests, true);
    }

    public static OrderTableDto of(OrderTable orderTable) {
        if (orderTable.getTableGroup() == null) {
            return new OrderTableDto(orderTable.getId(), null, orderTable.getNumberOfGuests(), orderTable.getEmpty());
        }

        return new OrderTableDto(orderTable.getId(), orderTable.getTableGroup().getId(), orderTable.getNumberOfGuests(), orderTable.getEmpty());
    }

    public Long getId() {
        return this.id;
    }

    public Long getTableGroupId() {
        return this.tableGroupId;
    }

    public int getNumberOfGuests() {
        return this.numberOfGuests;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean getEmpty() {
        return this.empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;

        if (numberOfGuests < 1) {
           this.empty = true;
           return;
        }

        this.empty = false;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderTableDto)) {
            return false;
        }
        OrderTableDto orderTableDto = (OrderTableDto) o;
        return Objects.equals(id, orderTableDto.id) && Objects.equals(tableGroupId, orderTableDto.tableGroupId) && numberOfGuests == orderTableDto.numberOfGuests && empty == orderTableDto.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
