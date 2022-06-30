package kitchenpos.event.listener;

import java.util.List;
import kitchenpos.dto.event.OrderCreatedEvent;
import kitchenpos.event.customEvent.OrderCreateEvent;
import kitchenpos.exception.OrderException;
import kitchenpos.repository.MenuRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreateEventListenerInMenu implements ApplicationListener<OrderCreateEvent> {

    private final MenuRepository menuRepository;

    public OrderCreateEventListenerInMenu(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void onApplicationEvent(OrderCreateEvent event) {
        OrderCreatedEvent orderCreatedEvent = (OrderCreatedEvent) event.getSource();

        menuMappedByOrderLineItemsIsExist(orderCreatedEvent.getMenuIds());
    }

    private void menuMappedByOrderLineItemsIsExist(List<Long> menuIds) {
        menuRepository.findAllById(menuIds);
        for (Long menuId : menuIds) {
            menuRepository.findById(menuId)
                .orElseThrow(() -> new OrderException("메뉴가 존재하지 않습니다"));
        }
    }
}
