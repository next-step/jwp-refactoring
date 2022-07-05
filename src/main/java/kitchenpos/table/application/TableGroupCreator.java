package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import org.springframework.stereotype.Component;

@Component
public class TableGroupCreator {

    private final OrderTableCreator orderTableCreator;

    public TableGroupCreator(OrderTableCreator orderTableCreator) {
        this.orderTableCreator = orderTableCreator;
    }

    public TableGroup toTableGroup(TableGroupRequest tableGroupRequest) {
        OrderTables orderTables = null;
        List<OrderTableRequest> tableRequests = tableGroupRequest.getOrderTables();

        if (tableRequests != null) {
            orderTables = new OrderTables(
                    tableRequests.stream().map(orderTableCreator::toOrderTable).collect(Collectors.toList()));
        }
        return TableGroup.builder()
                .id(tableGroupRequest.getId())
                .createdDate(tableGroupRequest.getCreatedDate())
                .orderTables(orderTables).build();
    }
}
