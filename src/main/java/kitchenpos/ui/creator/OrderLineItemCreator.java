package kitchenpos.ui.creator;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemCreator {
    private final MenuRepository menuRepository;

    public OrderLineItemCreator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public OrderLineItem toOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = setMenu(orderLineItemRequest);
        return OrderLineItem.builder()
                .seq(orderLineItemRequest.getSeq())
                .menu(menu)
                .quantity(orderLineItemRequest.getQuantity())
                .build();
    }
    private Menu setMenu(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = null;
        Long menuId = orderLineItemRequest.getMenuId();
        if (menuId != null) {
            menu = menuRepository.findById(menuId).orElse(null);
        }
        return menu;
    }
}
