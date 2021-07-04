package kitchenpos.tablegroup.dto;

import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

import java.util.List;

public class TableGroupRequest {
    private Long id;
    private List<OrderTable> orderTables;

    private TableGroupRequest(Long idj, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public static TableGroupRequest of(Long id, List<OrderTable> orderTables) {
        return new TableGroupRequest(id, orderTables);
    }

    public TableGroup toEntity() {
        return new TableGroup(id, orderTables);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

}
