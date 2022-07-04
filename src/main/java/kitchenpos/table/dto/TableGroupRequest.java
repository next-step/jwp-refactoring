package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.TableGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class TableGroupRequest {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTables;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public static TableGroupRequest of(TableGroup tableGroup) {
        List<OrderTableRequest> orderTableRequests = tableGroup.getOrderTables()
                .getOrderTables()
                .stream()
                .map(OrderTableRequest::of)
                .collect(Collectors.toList());

        return TableGroupRequest.builder()
                .id(tableGroup.getId())
                .createdDate(tableGroup.getCreatedDate())
                .orderTables(orderTableRequests)
                .build();
    }
}
