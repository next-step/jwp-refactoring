package kitchenpos.tobe.orders.order.infra;

import kitchenpos.tobe.orders.order.domain.OrderRepository;
import kitchenpos.tobe.orders.order.domain.OrderStatus;
import kitchenpos.tobe.orders.ordertable.domain.OrderTableClearedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final OrderRepository orderRepository;

    public OrderEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void onOrderTableClearedEvent(final OrderTableClearedEvent event) {
        if (orderRepository.existsByOrderTableIdAndStatusNot(
            event.getOrderTableId(),
            OrderStatus.COMPLETION
        )) {
            throw new IllegalStateException("완료되지 않은 주문이 있는 주문 테이블은 빈 테이블로 설정할 수 없습니다.");
        }
    }
}
