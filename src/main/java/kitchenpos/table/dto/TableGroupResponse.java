package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<TableGroupOrderTableResponse> orderTables;

    public TableGroupResponse(TableGroup tableGroup) {
        id = tableGroup.getId();
        createdDate = tableGroup.getCreatedDate();
        orderTables = tableGroup.getGroupTables().getOrderTables().stream()
                .map(TableGroupOrderTableResponse::new)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<TableGroupOrderTableResponse> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<TableGroupOrderTableResponse> orderTables) {
        this.orderTables = orderTables;
    }
}
