package order.application;

import common.domain.OrderStatus;
import common.exception.InvalidOrderStatusException;
import java.util.Arrays;
import java.util.Objects;
import order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import table.application.TableValidator;
import table.domain.OrderTable;

@Service
public class TableValidatorImpl implements TableValidator {

    private final OrderRepository orderRepository;

    public TableValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void checkValidChangeEmpty(OrderTable orderTable) {
        checkNotGroup(orderTable);
        checkOrderStatus(orderTable.getId());
    }

    private void checkNotGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void checkOrderStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStatusException();
        }
    }

}
