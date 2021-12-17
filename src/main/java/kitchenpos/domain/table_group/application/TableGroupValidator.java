package kitchenpos.domain.table_group.application;

import kitchenpos.domain.order.domain.OrderRepository;
import kitchenpos.domain.order.domain.OrderStatus;
import kitchenpos.domain.table.domain.OrderTableRepository;
import kitchenpos.domain.table.domain.OrderTables;
import kitchenpos.exception.BusinessException;
import kitchenpos.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTables getValidOrderTables(List<Long> orderTableIds) {
        final OrderTables tables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        tables.checkOrderTables(orderTableIds);
        return tables;
    }

    public OrderTables getCompleteOrderTables(Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        checkCompleteTable(orderTables.getOrderTableIds());
        return orderTables;
    }

    private void checkCompleteTable(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new BusinessException(ErrorCode.NOT_COMPLETE_ORDER);
        }
    }
}
