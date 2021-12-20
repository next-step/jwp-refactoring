package kitchenpos.application.fixture;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProduct 메뉴상품생성(Long seq, Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }
}
