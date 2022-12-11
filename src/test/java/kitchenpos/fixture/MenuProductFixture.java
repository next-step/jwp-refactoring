package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct create(Long seq, Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

}
