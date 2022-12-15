package kitchenpos.table.dto;


import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int guestCounts;
    private boolean empty;

    private OrderTableResponse(Long id, Long tableGroupId, int guestCounts, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.guestCounts = guestCounts;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable){
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getGuestCounts().value(),
                orderTable.getEmpty().value());

    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getGuestCounts() {
        return guestCounts;
    }

    public boolean isEmpty() {
        return empty;
    }
}
