package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.util.Objects;

public class OrderTableResponseDto {
    private Long id;
    private int numberOfGuests;
    private boolean empty;
    private Long tableGroupId;

    public OrderTableResponseDto(OrderTable orderTable) {
        this(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty(), orderTable.getTableGroup());
    }

    public OrderTableResponseDto(Long id, int numberOfGuests, boolean empty, TableGroup tableGroup) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        if (Objects.nonNull(tableGroup)) {
            this.tableGroupId = tableGroup.getId();
        }
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
