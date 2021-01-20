package kitchenpos.application.creator;

import java.util.List;
import kitchenpos.domain.model.OrderTable;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemCreateRequest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
public class OrderHelper {

    public static OrderCreateRequest createRequest(OrderTable orderTable, List<OrderLineItemCreateRequest> orderLineItems) {
        return new OrderCreateRequest(orderTable.getId(), orderLineItems);
    }
}
