package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

//@Builder
@AllArgsConstructor
@Getter
@Setter
public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private static Long getTableGroupIdByOrderTable(OrderTable orderTable) {
        Long tableGroupId = null;
        TableGroup tableGroup = orderTable.getTableGroup();
        if (tableGroup != null) {
            tableGroupId = tableGroup.getId();
        }
        return tableGroupId;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        Long tableGroupId = getTableGroupIdByOrderTable(orderTable);
//        return OrderTableResponse.builder()
//                .id(orderTable.getId())
//                .tableGroupId(tableGroupId)
//                .numberOfGuests(orderTable.getNumberOfGuests())
//                .empty(orderTable.isEmpty())
//                .build();
        return new OrderTableResponse(orderTable.getId(), tableGroupId, orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }
}