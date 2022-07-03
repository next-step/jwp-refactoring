package kitchenpos.menu;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.product.dto.request.ProductRequest;

public class MenuTestFixture {
    public static final ProductRequest 후라이드_치킨_FIXTURE = new ProductRequest("후라이드 치킨", BigDecimal.valueOf(15000L));
    public static final ProductRequest 감자튀김_FIXTURE = new ProductRequest("감자튀김", BigDecimal.valueOf(5000L));

    public static final MenuProductRequest 메뉴_상품_FIXTURE = new MenuProductRequest(1L, 3);
    public static final MenuProductRequest 메뉴_상품_FIXTURE2 = new MenuProductRequest(2L, 2);

    public static final MenuRequest 치킨_메뉴_FIXTURE = new MenuRequest("치킨", BigDecimal.valueOf(18000L), 1L,
        Arrays.asList(메뉴_상품_FIXTURE, 메뉴_상품_FIXTURE2));
    public static final MenuRequest 치킨_메뉴_FIXTURE2 = new MenuRequest("치킨", BigDecimal.valueOf(1000000L), 1L,
        Arrays.asList(메뉴_상품_FIXTURE, 메뉴_상품_FIXTURE2));
}
