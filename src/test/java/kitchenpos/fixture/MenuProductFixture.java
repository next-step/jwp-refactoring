package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {
    public static MenuProduct 메뉴_치킨 = create(1L, ProductFixture.치킨.getId(), 1L);

    public static MenuProduct create(Long seq, Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

}
