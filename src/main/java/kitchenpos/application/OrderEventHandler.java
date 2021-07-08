package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.event.order.OrderCreatedEvent;
import kitchenpos.exception.InvalidOrderLineItemsException;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderEventHandler {

    private final MenuRepository menuRepository;

    public OrderEventHandler(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOrderEvent(OrderCreatedEvent event) {
        Order order = event.getOrder();

        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        int size = orderLineItems.size();
        long savedMenuCount = menuRepository.countByIdIn(menuIds);
        if (size != savedMenuCount) {
            throw new InvalidOrderLineItemsException("orderLineItems size: " + size +
                    "saved orderLineItems size: " + savedMenuCount);
        }
    }
}
