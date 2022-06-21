package kitchenpos.acceptance.support;

import java.math.BigDecimal;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.domain.Product;

public class TestFixture {
    public static final Product 후라이드_치킨_FIXTURE = Product.of(null, "후라이드 치킨", BigDecimal.valueOf(15000L));
    public static final Product 감자튀김_FIXTURE = Product.of(null, "감자튀김", BigDecimal.valueOf(5000L));

    public static final OrderTable 주문_테이블_FIXTURE = OrderTable.of(null, null, 3, false);
}
