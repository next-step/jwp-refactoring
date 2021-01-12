package kitchenpos.application.creator;

import kitchenpos.dto.MenuDto;
import kitchenpos.dto.OrderLineItemDto;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
public class OrderLineItemHelper {

    public static OrderLineItemDto create(MenuDto menu, int quantity) {
        OrderLineItemDto orderLineItem = new OrderLineItemDto();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

}
