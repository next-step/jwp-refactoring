package table.dto;

import java.util.Objects;
import table.domain.OrderTable;

public class OrderTableResponseDto {
    private Long id;
    private int numberOfGuests;
    private boolean empty;
    private Long tableGroupId;

    public OrderTableResponseDto(OrderTable orderTable) {
        this(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty(), orderTable.getTableGroupId());
    }

    public OrderTableResponseDto(Long id, int numberOfGuests, boolean empty, Long tableGroupId) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        if (Objects.nonNull(tableGroupId)) {
            this.tableGroupId = tableGroupId;
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
