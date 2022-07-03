package kitchenpos.order.event;

import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.event.TableChangeEmptyEvent;
import kitchenpos.table.event.TableUngroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderEventListener {
    private static final String NOT_COMPLETION_ORDER_IS_EXIST = "계산완료되지 않은 주문이 존재합니다";
    private final OrderRepository orderRepository;

    public OrderEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void handle(TableChangeEmptyEvent event) {
        List<Order> orders = orderRepository.findAllByOrderTableId(event.getOrderTableId());
        if (isAnyIncompletion(orders)) {
            throw new IllegalArgumentException(NOT_COMPLETION_ORDER_IS_EXIST);
        }
    }

    @EventListener
    public void handle(TableUngroupEvent event) {
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(event.getOrderTableIds());
        if (isAnyIncompletion(orders)) {
            throw new IllegalArgumentException(NOT_COMPLETION_ORDER_IS_EXIST);
        }
    }

    private boolean isAnyIncompletion(List<Order> orders) {
        return orders.stream()
                .anyMatch(this::isIncompletion);
    }

    private boolean isIncompletion(Order order) {
        return !order.isCompletion();
    }
}
