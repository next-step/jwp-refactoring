package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import kitchenpos.table.domain.TableOrderStatusChecker;
import org.springframework.stereotype.Component;

@Component
public class TableOrderStatusCheckerImpl implements TableOrderStatusChecker {
    private final OrderRepository orderRepository;

    public TableOrderStatusCheckerImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean isExistTablesBeforeBillingStatus(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }

    @Override
    public boolean isBeforeBillingStatus(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
