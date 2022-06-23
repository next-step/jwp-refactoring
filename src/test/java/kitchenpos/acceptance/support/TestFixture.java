package kitchenpos.acceptance.support;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.domain.Product;
import kitchenpos.table.domain.request.OrderLineItemRequest;
import kitchenpos.table.domain.request.OrderTableRequest;

public class TestFixture {
    public static final Product 후라이드_치킨_FIXTURE = Product.of(null, "후라이드 치킨", BigDecimal.valueOf(15000L));
    public static final Product 감자튀김_FIXTURE = Product.of(null, "감자튀김", BigDecimal.valueOf(5000L));

    public static final OrderTable 주문_테이블_FIXTURE = OrderTable.of(null, null, 3, false);

    public static final OrderTableRequest 주문_테이블_REQUEST_FIXTURE = new OrderTableRequest(null, null, 3, false);
    public static final OrderLineItemRequest 주문_항목_REQUEST_FIXTURE = new OrderLineItemRequest(1L, 1);
    public static final OrderLineItemRequest 주문_항목2_REQUEST_FIXTURE = new OrderLineItemRequest(2L, 1);
}
