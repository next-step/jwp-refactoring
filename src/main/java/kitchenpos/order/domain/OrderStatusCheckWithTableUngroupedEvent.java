package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.TableUngroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

public class OrderStatusCheckWithTableUngroupedEvent {
    private final OrderRepository orderRepository;

    public OrderStatusCheckWithTableUngroupedEvent(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional(readOnly = true)
    public void handle(final TableUngroupedEvent event) {
        boolean isAllOrderStatusCompleted = event.getOrderTableIds().stream()
            .allMatch(this::isOrderStatusCompleted);

        if (!isAllOrderStatusCompleted) {
            throw new IllegalArgumentException("조리나 식사중인 테이블은 단체를 해제할 수 없습니다.");
        }
    }

    private boolean isOrderStatusCompleted(final Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);

        return orders.stream()
            .allMatch(Order::isCompleteStatus);
    }
}
