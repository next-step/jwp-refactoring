package kitchenpos.table.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderStatusException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.exception.OrderTableException;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class TableValidator {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableValidator(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public void orderStatusValidate(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderStatusException(OrderStatusException.ORDER_STATUS_CAN_NOT_UNGROUP_MSG);
        }
    }

    public List<OrderTable> findTableAllByIdIn (List<Long> ids) {
        return orderTableRepository.findAllByIdIn(ids);
    }

    public void orderTablesSizeValidation(List<OrderTable> orderTables, TableGroupRequest tableGroupRequest) {
        if (Objects.isNull(tableGroupRequest.getOrderTableIds())) {
            throw new IllegalArgumentException();
        }

        if (orderTables.size() != tableGroupRequest.getOrderTableIds().size()) {
            throw new IllegalArgumentException();
        }
    }

    public void addOrderTableValidation(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new OrderTableException(OrderTableException.ORDER_TABLE_SIZE_OVER_TWO_MSG);
        }

        if (orderTables.stream()
                .map(OrderTable::getTableGroupId)
                .anyMatch(Objects::nonNull)) {
            throw new OrderTableException(OrderTableException.ORDER_TALBE_ALREADY_HAS_GROUP_MSG);
        }
    }

    public void orderStatusByIdsValidate(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                ids, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderStatusException(OrderStatusException.ORDER_STATUS_CAN_NOT_UNGROUP_MSG);
        }
    }
}
