package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class ChangeNumberOfGuestsResponse {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    private ChangeNumberOfGuestsResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static ChangeNumberOfGuestsResponse of(OrderTable orderTable) {
        return new ChangeNumberOfGuestsResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }
}
