package kitchenpos.order.support;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.support.OrderSupport;
import kitchenpos.table.domain.OrderTable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Primary
@Component
public class OrderSupportImpl implements OrderSupport {
    private static final List<OrderStatus> USE_STATUSES = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

    private final OrderRepository orderRepository;

    public OrderSupportImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean isUsingTable(OrderTable table) {
        return orderRepository.existsByOrderTableAndOrderStatusIn(table, USE_STATUSES);
    }

    @Override
    public boolean isUsingTables(Collection<OrderTable> tables) {
        return orderRepository.existsByOrderTableInAndOrderStatusIn(tables, USE_STATUSES);
    }
}
