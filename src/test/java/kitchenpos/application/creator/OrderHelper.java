package kitchenpos.application.creator;

import java.util.Arrays;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.dto.OrderTableDto;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
public class OrderHelper {

    public static OrderDto create(OrderTableDto orderTable, OrderLineItemDto...orderLineItems) {
        OrderDto order = new OrderDto();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItems));
        return order;
    }

}
