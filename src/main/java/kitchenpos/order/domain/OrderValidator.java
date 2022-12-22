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
            throw new IllegalArgumentException("꼭 한개이상에 주문 상품이 포함되어 있어야 합니다.");
        }
    }

    public static void validateMenus(List<OrderLineItemRequest> orderLineItems, long menuCount) {
        if (orderLineItems.size() != menuCount) {
            throw new IllegalArgumentException("존재하는 메뉴들로만 구성되어 있어야 합니다.");
        }

    }

}
