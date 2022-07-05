package kitchenpos.order.domain;

import static kitchenpos.order.exception.CannotMakeOrderException.NOT_EXIST_MENU;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.exception.CannotMakeOrderException;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemFactory {

    private final MenuRepository menuRepository;

    public OrderLineItemFactory(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public OrderLineItem createOrderLineItem(Long menuId, long quantity) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new CannotMakeOrderException(NOT_EXIST_MENU));
        OrderLineMenu orderLineMenu = new OrderLineMenu(menu.getId(), menu.getName(), menu.getPrice());
        return new OrderLineItem(orderLineMenu, quantity);
    }
}
