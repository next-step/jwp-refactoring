package kitchenpos.application.creator;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
public class OrderLineItemHelper {

    public static OrderLineItem create(Menu menu, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

}
