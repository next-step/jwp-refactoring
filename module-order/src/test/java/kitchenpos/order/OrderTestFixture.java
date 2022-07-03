package kitchenpos.order;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.product.dto.request.ProductRequest;
import kitchenpos.table.dto.request.OrderTableRequest;

public class OrderTestFixture {
    public static final ProductRequest 후라이드_치킨_FIXTURE = new ProductRequest("후라이드 치킨", BigDecimal.valueOf(15000L));
    public static final ProductRequest 감자튀김_FIXTURE = new ProductRequest("감자튀김", BigDecimal.valueOf(5000L));

    public static final MenuProductRequest 메뉴_상품_FIXTURE = new MenuProductRequest(1L, 3);
    public static final MenuProductRequest 메뉴_상품_FIXTURE2 = new MenuProductRequest(2L, 2);

    public static final MenuRequest 치킨_메뉴_FIXTURE = new MenuRequest("치킨", BigDecimal.valueOf(18000L), 1L,
        Arrays.asList(메뉴_상품_FIXTURE, 메뉴_상품_FIXTURE2));

    public static final MenuProduct 메뉴_상품_ENTITY_FIXTURE = MenuProduct.of(1L, 1);

    public static final OrderTableRequest 주문_테이블_REQUEST_FIXTURE = new OrderTableRequest(null, null, 3, false);
}
