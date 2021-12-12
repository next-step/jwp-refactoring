package kitchenpos.menu.application.sample;

import static kitchenpos.product.application.sample.ProductSample.후라이드치킨;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductSample {

    public static MenuProduct 후라이드치킨두마리() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setQuantity(Quantity.from(2L));
        menuProduct.setProductId(후라이드치킨().getId());
        return menuProduct;
    }
}
