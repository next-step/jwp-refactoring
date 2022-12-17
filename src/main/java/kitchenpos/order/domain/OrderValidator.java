package kitchenpos.order.domain;

import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class OrderValidator {

    public static void validate(OrderRequest orderRequest, long menuCount) {
        List<OrderLineItemRequest> orderLineItems = orderRequest.getOrderLineItems();
        validateParam(orderLineItems);
        validateMenus(orderLineItems, menuCount);
    }

    public static void validateParam(List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public static void validateMenus(List<OrderLineItemRequest> orderLineItems, long menuCount) {
        if (orderLineItems.size() != menuCount) {
            throw new IllegalArgumentException();
        }

    }

}
