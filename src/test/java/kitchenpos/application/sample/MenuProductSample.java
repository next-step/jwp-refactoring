package kitchenpos.application.sample;

import static kitchenpos.application.sample.ProductSample.후라이드치킨;

import kitchenpos.domain.MenuProduct;

public class MenuProductSample {

    public static MenuProduct 후라이드치킨두마리() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setQuantity(2);
        menuProduct.setProductId(후라이드치킨().getId());
        return menuProduct;
    }
}
