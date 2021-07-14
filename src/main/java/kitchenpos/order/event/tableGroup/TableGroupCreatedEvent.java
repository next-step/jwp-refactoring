package kitchenpos.order.event.tableGroup;

import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;

import java.util.List;

public class TableGroupCreatedEvent {
    List<OrderTableRequest> orderTableIdRequests;
    TableGroupRequest tableGroupRequest;
    TableGroup tableGroup;

    public TableGroupCreatedEvent(TableGroupRequest tableGroupRequest, TableGroup tableGroup) {
        this.orderTableIdRequests = tableGroupRequest.getOrderTables();
        this.tableGroupRequest = tableGroupRequest;
        this.tableGroup = tableGroup;
    }

    public List<OrderTableRequest> getOrderTableIdRequests() {
        return orderTableIdRequests;
    }

    public List<OrderTableRequest> getOrderTableRequests() {
        return tableGroupRequest.getOrderTables();
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
