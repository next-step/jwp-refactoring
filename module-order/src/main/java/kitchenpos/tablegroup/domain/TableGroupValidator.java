package kitchenpos.tablegroup.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupValidator(
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void groupExistValidate(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new InvalidParameterException(CommonErrorCode.TABLE_NOT_CREATED_EXCEPTION);
        }

        boolean isExistIncludeTableGroup = orderTables.stream()
            .anyMatch(OrderTable::isIncludeTableGroup);

        if (isExistIncludeTableGroup) {
            throw new InvalidParameterException(
                CommonErrorCode.ORDER_TABLE_EXISTS_TABLE_GROUP_EXCEPTION);
        }
    }

    public void completedOrdersValid(TableGroup tableGroup) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(
            tableGroup.getId());

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderRepository.existsAllByOrderTableIdInAndOrderStatusIn(orderTableIds,
            OrderStatus.excludeCompletionValues())) {
            throw new InvalidParameterException(
                CommonErrorCode.ORDER_TABLE_UNGROUP_NOT_COMPLETE_EXCEPTION);
        }
    }
}
