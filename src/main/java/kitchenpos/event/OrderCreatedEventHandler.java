package kitchenpos.event;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.repository.menu.MenuRepository;
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
    public void addOrderMenu(OrderCreatedEvent event) {

        List<OrderLineItem> orderLineItems = event.getOrderLineItems();

        orderLineItems.forEach(orderLineItem -> {
            Menu menu = menuRepository.findById(orderLineItem.getMenu().getId())
                                    .orElseThrow(() -> new IllegalArgumentException("등록된 메뉴가 아닙니다."));
            orderLineItem.addMenu(menu);
        });
    }

}
