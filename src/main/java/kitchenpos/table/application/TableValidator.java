package kitchenpos.table.application;

import kitchenpos.common.domain.OrderStatus;
import kitchenpos.common.exception.InvalidOrderStatusException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableValidator {

    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void checkValidChangeEmpty(OrderTable orderTable) {
        checkNotGroup(orderTable);
        checkOrderStatus(orderTable.getId());
    }

    private void checkNotGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    private void checkOrderStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStatusException();
        }
    }

    public void checkValidUngroup(TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStatusException();
        }
    }
}
