package kitchenpos;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.request.OrderLineItemRequest;

public class OrderLineItemTestFixture {
    public static OrderLineItem 주문_항목_생성(final Long id, final String name, final BigDecimal price,
                                         final Long quantity) {
        return new OrderLineItem(id, name, price, quantity);
    }

    public static OrderLineItemRequest 주문_항목_요청_생성(final Long id, final String name, final BigDecimal price,
                                                   final Long quantity) {
        return new OrderLineItemRequest(id, name, price, quantity);
    }
}
