package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.TableGroup;

public class TableGroupRequest {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest(Long id, LocalDateTime createdDate,
                             List<OrderTableRequest> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
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

        return new TableGroupRequest(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableRequests);
    }
}
