package kitchenpos.menu.domain;

import static kitchenpos.product.domain.ProductTestFixture.*;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;

public class MenuProductTestFixture {
    public static final MenuProductRequest 짜장면_1그릇_요청 = menuProductRequest(1L, 1L);
    public static final MenuProductRequest 짬뽕_2그릇_요청 = menuProductRequest(2L, 2L);
    public static final MenuProductRequest 탕수육_소_1그릇_요청 = menuProductRequest(2L, 2L);
    public static final MenuProduct 짜장면_1그릇 = menuProduct(1L, 짜장면, 1L);
    public static final MenuProduct 짬뽕_2그릇 = menuProduct(2L, 짬뽕, 2L);
    public static final MenuProduct 탕수육_소_1그릇 = menuProduct(3L, 탕수육_소, 1L);

    public static MenuProductRequest menuProductRequest(long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public static MenuProduct menuProduct(Product product, long quantity) {
        return MenuProduct.of(product, quantity);
    }

    public static MenuProduct menuProduct(Long id, Product product, long quantity) {
        return MenuProduct.of(id, product, quantity);
    }
}
