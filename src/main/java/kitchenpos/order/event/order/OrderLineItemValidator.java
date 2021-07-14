package kitchenpos.order.event.order;

import kitchenpos.order.domain.OrderLineItem;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class OrderLineItemValidator {
    public static void validOrderLineItemEmpty(List<OrderLineItem> orderLineItemsRequest) {
        if (CollectionUtils.isEmpty(orderLineItemsRequest)) {
            throw new IllegalArgumentException("주문에는 메뉴가 1개 이상 필요합니다.");
        }
    }
    public static void validOrderLIneItemCount(List<OrderLineItem> orderLineItemsRequest, int menuSize) {
        if (orderLineItemsRequest.size() != menuSize) {
            throw new IllegalArgumentException("등록된 메뉴만 주문할 수 있습니다.");
        }
    }
}
