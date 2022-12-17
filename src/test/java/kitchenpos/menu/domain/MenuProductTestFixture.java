package kitchenpos.menu.domain;

import static kitchenpos.product.domain.ProductTestFixture.*;

import kitchenpos.menu.dto.MenuProductRequest;

public class MenuProductTestFixture {
    public static final MenuProductRequest 짜장면_1그릇_요청 = menuProductRequest(1L, 1L);
    public static final MenuProductRequest 짬뽕_2그릇_요청 = menuProductRequest(2L, 2L);
    public static final MenuProductRequest 탕수육_소_1그릇_요청 = menuProductRequest(2L, 2L);
    public static final MenuProduct 짜장면_1그릇 = menuProduct(1L, 짜장면.id(), 1L);
    public static final MenuProduct 짬뽕_2그릇 = menuProduct(2L, 짬뽕.id(), 2L);
    public static final MenuProduct 탕수육_소_1그릇 = menuProduct(3L, 탕수육_소.id(), 1L);

    public static MenuProductRequest menuProductRequest(long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public static MenuProduct menuProduct(Long productId, long quantity) {
        return MenuProduct.of(productId, quantity);
    }

    public static MenuProduct menuProduct(Long id, Long productId, long quantity) {
        return MenuProduct.of(id, productId, quantity);
    }
}
