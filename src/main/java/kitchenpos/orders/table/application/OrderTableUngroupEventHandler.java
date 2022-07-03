package kitchenpos.orders.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.orders.order.domain.OrderRepository;
import kitchenpos.orders.order.domain.OrderStatus;
import kitchenpos.orders.order.event.TableUngroupEvent;
import kitchenpos.orders.table.domain.OrderTable;
import kitchenpos.orders.table.domain.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderTableUngroupEventHandler {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableUngroupEventHandler(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @EventListener
    public void handle(TableUngroupEvent event) {
        List<OrderTable> findOrderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        List<Long> orderTableIds = getOrderTableIds(findOrderTables);
        validateOrderStatus(orderTableIds);
        findOrderTables.stream().forEach(OrderTable::detachFromTableGroup);
    }

    private void validateOrderStatus(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문의 상태가 COOKING, MEAL 입니다.");
        }
    }

    List<Long> getOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
