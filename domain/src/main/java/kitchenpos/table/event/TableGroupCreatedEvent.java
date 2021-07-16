package kitchenpos.table.event;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;

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
