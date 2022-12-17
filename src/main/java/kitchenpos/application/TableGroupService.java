package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.port.OrderPort;
import kitchenpos.port.OrderTablePort;
import kitchenpos.port.TableGroupPort;
import kitchenpos.domain.type.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        TableGroup tableGroup = TableGroup.from(orderTables);

        final TableGroup savedTableGroup = tableGroupPort.save(tableGroup);


        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupPort.findById(tableGroupId);
        final List<Order> order = orderPort.findAllByOrderTableIdIn(tableGroup.getOrderTablesId());

        tableGroup.ungroup(order);
    }
}
