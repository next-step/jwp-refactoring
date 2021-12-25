package kitchenpos.order.dto;

import java.util.List;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

public class TableGroupRequest {
    private List<OrderTable> orderTables;
    
    private TableGroupRequest() {
    }

    private TableGroupRequest(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
    
    public static TableGroupRequest from(List<OrderTable> orderTables) {
        return new TableGroupRequest(orderTables);
    }
    
    public static TableGroupRequest from(TableGroup tableGroup) {
        return new TableGroupRequest(tableGroup.getOrderTables().getOrderTables());
    }
    
    public TableGroup toTableGroup() {
        return TableGroup.from(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

}
