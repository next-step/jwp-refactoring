package kitchenpos.table.ui.dto;

import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableCreateResponse> orderTables;

    private TableGroupCreateResponse() {
    }

    public TableGroupCreateResponse(TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.createdDate = tableGroup.getCreatedDate();
        this.orderTables = tableGroup.getOrderTables().stream()
                .map(OrderTableCreateResponse::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableCreateResponse> getOrderTables() {
        return orderTables;
    }
}
