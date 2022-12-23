package kitchenpos.order.domain;

import kitchenpos.ExceptionMessage;
import kitchenpos.table.domain.TableEmptyChangedEvent;
import kitchenpos.table.domain.TableUngroupedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private OrderRepository orderRepository;

    public OrderEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void checkCookingOrMealByOrderTableIds(TableUngroupedEvent event) {
        for (Long orderTableId : event.getOrderTableIds()) {
            checkCookingOrMealByOrderTableId(orderTableId);
        }
    }

    @EventListener
    public void checkCookingOrMealByOrderTableId(TableEmptyChangedEvent event) {
        checkCookingOrMealByOrderTableId(event.getOrderTableId());
    }

    private void checkCookingOrMealByOrderTableId(Long orderTableId) {
        final Order order = orderRepository.findOrderByOrderTableId(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_ORDER.getMessage()));
        order.checkCookingOrMeal();
    }
}
