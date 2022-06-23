package kitchenpos.acceptance.support;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.request.ProductRequest;
import kitchenpos.table.domain.request.OrderTableRequest;

public class TestFixture {
    public static final ProductRequest 후라이드_치킨_FIXTURE = new ProductRequest("후라이드 치킨", BigDecimal.valueOf(15000L));
    public static final ProductRequest 감자튀김_FIXTURE = new ProductRequest("감자튀김", BigDecimal.valueOf(5000L));

    public static final OrderTableRequest 주문_테이블_REQUEST_FIXTURE = new OrderTableRequest(null, null, 3, false);
}
