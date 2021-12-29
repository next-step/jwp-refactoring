package kitchenpos.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.menu.domain.MenuRepository;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateMenu(OrderLineItems orderLineItems) {
        orderLineItems.getOrderLineItems()
            .forEach(orderLineItem -> menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(KitchenposNotFoundException::new));

        final List<Long> menuIds = orderLineItems.getMenuIds();
        orderLineItems.validateSize(menuRepository.countByIdIn(menuIds));
    }
}
