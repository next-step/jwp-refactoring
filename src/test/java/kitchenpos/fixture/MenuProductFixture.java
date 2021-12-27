package kitchenpos.fixture;

import kitchenpos.product.domain.Product;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;

import java.math.BigDecimal;

public class MenuProductFixture {

    public static MenuProduct 생성(Product product, long quantity) {
        return new MenuProduct(product.getId(), quantity);
    }

    public static MenuProduct 후라이드두마리(){
        Product 후라이드 = ProductFixture.생성("후라이드", new BigDecimal("5000"));
        return new MenuProduct(후라이드.getId(), 2L);
    }

    public static MenuProductRequest request생성(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
