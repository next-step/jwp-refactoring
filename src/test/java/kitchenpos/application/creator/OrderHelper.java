package kitchenpos.application.creator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemCreateRequest;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.dto.OrderTableDto;

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
