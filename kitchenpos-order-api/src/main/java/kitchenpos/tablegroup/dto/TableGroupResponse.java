package kitchenpos.tablegroup.dto;

import kitchenpos.tablegroup.domain.TableGroup;

import java.util.List;

public class TableGroupResponse {
    private Long id;
    private List<Long> orderTables;

    public TableGroupResponse() {}

    public TableGroupResponse(TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.orderTables = tableGroup.getOrderTableIds();
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup);
    }

    public Long getId() {
        return id;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}