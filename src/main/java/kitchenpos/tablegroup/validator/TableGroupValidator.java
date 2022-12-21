package kitchenpos.tablegroup.validator;

import kitchenpos.common.constants.ErrorCodeType;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.port.OrderPort;
import kitchenpos.order.port.OrderTablePort;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupValidator {

    private static final int TABLE_GROUP_MIN_SIZE = 2;

    private final OrderPort orderPort;

    private final OrderTablePort orderTablePort;

    public TableGroupValidator(OrderPort orderPort, OrderTablePort orderTablePort) {
        this.orderPort = orderPort;
        this.orderTablePort = orderTablePort;
    }

    public void makeTableGroup(TableGroupRequest request) {
        validCheckOrderTableIdsIsEmpty(request.getOrderTableIds());
        validCheckOrderTableMisSize(request.getOrderTableIds());

        List<OrderTable> orderTables =
                orderTablePort.findAllByIdIn(request.getOrderTableIds());

        new OrderTables(orderTables).validCheckTableGroup();
    }

    private void validCheckOrderTableMisSize(List<Long> orderTableIds) {
        if (orderTableIds.size() < TABLE_GROUP_MIN_SIZE) {
            throw new IllegalArgumentException(ErrorCodeType.ORDER_TABLE_MIN_SIZE_ERROR.getMessage());
        }
    }

    private void validCheckOrderTableIdsIsEmpty(List<Long> orderTableIds) {
        if (orderTableIds.isEmpty()) {
            throw new IllegalArgumentException(ErrorCodeType.NOT_FOUND_ORDER_TABLE.getMessage());
        }
    }

    public void ungroup(TableGroup tableGroup) {
        List<OrderTable> orderTables = orderTablePort.findAllByTableGroupId(tableGroup.getId());
        List<Long> orderTableIds = getOrderTableIds(orderTables);
        List<Order> orders = orderPort.findAllByOrderTableIdIn(orderTableIds);

        orders.forEach(Order::validUngroupable);
    }

    private List<Long> getOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
    }
}
