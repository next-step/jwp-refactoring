package kitchenpos.ui.dto.tableGroup;

import kitchenpos.domain.orderTable.OrderTable;

import java.util.Objects;

public class OrderTableInTableGroupResponse {
    // TODO: 기능 복구되기 전의 임시 조치
    private Long id;
//    private Long tableGroupId;
//    private int numberOfGuests;
//    private boolean empty;
//    private boolean grouped;

    public OrderTableInTableGroupResponse() {
    }

//    public OrderTableInTableGroupResponse(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty, final boolean grouped) {
//        this.id = id;
//        this.tableGroupId = tableGroupId;
//        this.numberOfGuests = numberOfGuests;
//        this.empty = empty;
//        this.grouped = grouped;
//    }


    public OrderTableInTableGroupResponse(final Long id) {
        this.id = id;
    }

    public static OrderTableInTableGroupResponse of(Long orderTableId) {
        return new OrderTableInTableGroupResponse(orderTableId);
    }

//    public static OrderTableInTableGroupResponse of(OrderTable orderTable) {
//        return new OrderTableInTableGroupResponse(
//                orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
//                orderTable.isEmpty(), orderTable.isGrouped()
//        );
//    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTableInTableGroupResponse that = (OrderTableInTableGroupResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderTableInTableGroupResponse{" +
                "id=" + id +
                '}';
    }
}
