package kitchenpos.order;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;

public class OrderTestFixture {
    public static final MenuProductRequest 메뉴_상품_FIXTURE = new MenuProductRequest(1L, 3);
    public static final MenuProductRequest 메뉴_상품_FIXTURE2 = new MenuProductRequest(2L, 2);

    public static final MenuRequest 치킨_메뉴_FIXTURE = new MenuRequest("치킨", BigDecimal.valueOf(18000L), 1L,
        Arrays.asList(메뉴_상품_FIXTURE, 메뉴_상품_FIXTURE2));
    public static final MenuRequest 치킨_메뉴_FIXTURE2 = new MenuRequest("치킨", BigDecimal.valueOf(1000000L), 1L,
        Arrays.asList(메뉴_상품_FIXTURE, 메뉴_상품_FIXTURE2));

    public static final MenuProduct 메뉴_상품_ENTITY_FIXTURE = MenuProduct.of(1L, 1);
}
