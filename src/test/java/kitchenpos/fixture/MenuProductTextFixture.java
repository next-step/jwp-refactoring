package kitchenpos.fixture;

import kitchenpos.product.domain.Product;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;

public class MenuProductTextFixture {
    public static MenuProduct 생성(Long seq, Product product, long quantity) {
        return new MenuProduct(seq, product, quantity);
    }
    public static MenuProductRequest request생성(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
