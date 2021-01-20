package kitchenpos.application.creator;

import kitchenpos.dto.MenuDto;
import kitchenpos.dto.OrderLineItemCreateRequest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
public class OrderLineItemHelper {

    public static OrderLineItemCreateRequest createRequest(MenuDto menu, int quantity) {
        return new OrderLineItemCreateRequest(menu.getId(), quantity);
    }
}
