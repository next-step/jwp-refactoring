package kitchenpos.menu.application.event;

import kitchenpos.menu.application.exception.BadMenuIdException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.event.OrderCreatedEvent;
import kitchenpos.order.domain.OrderLineItem;
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
    public void createOrderEventListener(OrderCreatedEvent event) {
        List<OrderLineItem> orderLineItems = event.getOrder().getOrderLineItems();
        int actualSize = menuRepository.countByIdIn(orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList()));
        if (orderLineItems.size() != actualSize) {
            throw new BadMenuIdException();
        }
    }
}
