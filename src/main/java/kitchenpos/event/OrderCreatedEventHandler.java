package kitchenpos.event;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.repository.MenuRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderCreatedEventHandler {

    private final MenuRepository menuRepository;

    public OrderCreatedEventHandler(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @EventListener
    public void createOrder(OrderCreatedEvent event) {

        Order order = event.getOrder();
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        orderLineItems.forEach(orderLineItem -> {
            Menu menu = menuRepository.findById(orderLineItem.getMenu().getId())
                                    .orElseThrow(() -> new IllegalArgumentException("등록된 메뉴가 아닙니다."));
            orderLineItem.addMenu(menu);
        });
    }

}
