package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.TableClearedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderStatusCheckWithTableClearedEvent {

    private final OrderRepository orderRepository;

    public OrderStatusCheckWithTableClearedEvent(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional(readOnly = true)
    public void handle(final TableClearedEvent event) {
        if (!isOrderStatusCompleted(event.getOrderTableId())) {
            throw new IllegalArgumentException("주문 완료가 아닌 테이블은 상태를 변경할 수 없습니다.");
        }
    }

    private boolean isOrderStatusCompleted(final Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);

        return orders.stream()
            .allMatch(Order::isCompleteStatus);
    }
}
