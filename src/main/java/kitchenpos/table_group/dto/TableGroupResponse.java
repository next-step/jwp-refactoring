package kitchenpos.table_group.dto;

import kitchenpos.table_group.domain.GroupTable;
import kitchenpos.table_group.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<TableGroupOrderTableResponse> orderTables;

    public TableGroupResponse(TableGroup tableGroup, List<GroupTable> groupTables) {
        id = tableGroup.getId();
        createdDate = tableGroup.getCreatedDate();
        orderTables = groupTables.stream()
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
