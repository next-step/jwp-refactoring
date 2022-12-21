package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.order.port.OrderPort;
import kitchenpos.order.port.OrderTablePort;
import kitchenpos.tablegroup.port.TableGroupPort;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderPort orderPort;
    private final OrderTablePort orderTablePort;
    private final TableGroupPort tableGroupPort;

    public TableGroupService(final OrderPort orderPort, final OrderTablePort orderTablePort, final TableGroupPort tableGroupPort) {
        this.orderPort = orderPort;
        this.orderTablePort = orderTablePort;
        this.tableGroupPort = tableGroupPort;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTable> orderTables =
                orderTablePort.findAllByIdIn(request.getOrderTableIds());

        TableGroup tableGroup = new TableGroup(new OrderTables(orderTables));
        final TableGroup savedTableGroup = tableGroupPort.save(tableGroup);

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupPort.findById(tableGroupId);
        final List<Order> order = orderPort.findAllByOrderTableIdIn(tableGroup.getOrderTablesId());
        order.forEach(Order::validUngroupable);

        tableGroup.ungroup();
    }
}
