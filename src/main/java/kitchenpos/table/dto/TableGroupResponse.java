package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        List<OrderTableResponse> orderTableResponses = null;

        OrderTables orderTables = tableGroup.getOrderTables();
        if (orderTables != null) {
            orderTableResponses = orderTables.getOrderTables()
                    .stream()
                    .map(OrderTableResponse::of)
                    .collect(Collectors.toList());
        }

        return TableGroupResponse.builder()
                .id(tableGroup.getId())
                .createdDate(tableGroup.getCreatedDate())
                .orderTables(orderTableResponses)
                .build();
    }
}
