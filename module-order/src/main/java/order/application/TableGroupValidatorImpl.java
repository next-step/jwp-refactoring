package order.application;

import common.domain.OrderStatus;
import common.exception.InvalidOrderStatusException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import table.domain.OrderTable;
import table_group.application.TableGroupValidator;

@Service
public class TableGroupValidatorImpl implements TableGroupValidator {

    private static final int MIN_TABLE_SIZE = 2;

    private final OrderRepository orderRepository;

    public TableGroupValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void checkValidUngroup(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStatusException();
        }
    }

    @Override
    public void checkCreatable(List<OrderTable> orderTables, List<Long> requestOrderTableIds) {
        checkNotFoundOrderTables(orderTables, requestOrderTableIds);
        checkOrderTableSize(orderTables);
        for (final OrderTable orderTable : orderTables) {
            checkEmptyOrGrouped(orderTable);
        }
    }

    private void checkNotFoundOrderTables(List<OrderTable> orderTables, List<Long> requestOrderTableIds) {
        if (orderTables.size() != requestOrderTableIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void checkOrderTableSize(List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_TABLE_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    private void checkEmptyOrGrouped(OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

}
