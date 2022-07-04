package kitchenpos.order.creator;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.menu.repository.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemCreator {
    private final MenuRepository menuRepository;

    public OrderLineItemCreator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    private Menu setMenu(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = null;
        Long menuId = orderLineItemRequest.getMenuId();
        if (menuId != null) {
            menu = menuRepository.findById(menuId).orElse(null);
        }
        return menu;
    }

    public OrderLineItem toOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = setMenu(orderLineItemRequest);
        return OrderLineItem.builder()
                .seq(orderLineItemRequest.getSeq())
                .menu(menu)
                .quantity(orderLineItemRequest.getQuantity())
                .build();
    }

}
