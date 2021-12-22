package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;

import java.util.List;

public class MenuProductResponse {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems;
}
