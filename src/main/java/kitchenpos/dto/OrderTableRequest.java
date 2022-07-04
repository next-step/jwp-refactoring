package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OrderTableRequest {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public static OrderTableRequest of(OrderTable orderTable) {
        TableGroup tableGroup = orderTable.getTableGroup();
        Long tableGroupId = null;
        if (tableGroup != null) {
            tableGroupId = tableGroup.getId();
        }

        return OrderTableRequest.builder()
                .id(orderTable.getId())
                .tableGroupId(tableGroupId)
                .numberOfGuests(orderTable.getNumberOfGuests())
                .empty(orderTable.isEmpty())
                .build();
    }
}