package kitchenpos.application.sample;

import static kitchenpos.application.sample.ProductSample.짜장면;

import kitchenpos.domain.MenuProduct;

public class MenuProductSample {

    public static MenuProduct 짜장면_두개() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setQuantity(2);
        menuProduct.setProductId(짜장면().getId());
        return menuProduct;
    }
}
