package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableResponseDto {
    private Long id;
    private int numberOfGuests;
    private boolean empty;
    private TableGroupResponseDto tableGroup;

    public OrderTableResponseDto(Long id, int numberOfGuests, boolean empty,
        TableGroupResponseDto tableGroup) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.tableGroup = tableGroup;
    }

    public OrderTableResponseDto(OrderTable orderTable) {
        this.id = orderTable.getId();
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.isEmpty();

        TableGroup tableGroup = orderTable.getTableGroup();
        if (tableGroup != null) {
            this.tableGroup = new TableGroupResponseDto(tableGroup);
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

    public TableGroupResponseDto getTableGroup() {
        return tableGroup;
    }
}
