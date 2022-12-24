package kitchenpos.domin;

import kitchenpos.table.domain.TableChangeEmptyEvent;
import kitchenpos.table.domain.TableUngroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderRelatedEventHandler {

    private static final String ORDER_IS_NOT_COMPLETE_EXCEPTION = "완료되지 않은 주문이 있어 상태를 변경할 수 없습니다.";

    private OrderRepository orderRepository;

    public OrderRelatedEventHandler(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void checkOrderNotCompleted(TableUngroupedEvent event) {
        checkOrderStatus(event.getOrderTableId());
    }

    @EventListener
    public void checkOrderNotCompleted(TableChangeEmptyEvent event) {
        checkOrderStatus(event.getOrderTableId());
    }

    private void checkOrderStatus(Long orderTableId) {
        List<Order> orders = orderRepository.findOrderByOrderTableId(orderTableId);
        checkEmptyOrder(orders);
        orders.stream().forEach(this::checkIndividualOrder);

    }

    private void checkIndividualOrder(Order order){
        if (!order.isOrderComplete()) {
            throw new IllegalArgumentException(ORDER_IS_NOT_COMPLETE_EXCEPTION);
        }
    }

    private void checkEmptyOrder(List<Order> orders) {
        if (orders.size() == 0) {
            throw new IllegalArgumentException(ORDER_IS_NOT_COMPLETE_EXCEPTION);
        }
    }
}
