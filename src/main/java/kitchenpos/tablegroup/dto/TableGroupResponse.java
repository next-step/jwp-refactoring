package kitchenpos.tablegroup.dto;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroupResponse {
    private Long id;

    private List<OrderTable> orderTables = new ArrayList<>();

    private LocalDateTime createdDate;

    public TableGroupResponse() {
    }

    public TableGroupResponse(TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.orderTables = tableGroup.getOrderTables();
        this.createdDate = tableGroup.getCreatedDate();
    }

    public TableGroupResponse(Long id, List<OrderTable> orderTables, LocalDateTime createdDate) {
        this.id = id;
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
