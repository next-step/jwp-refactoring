package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

import static kitchenpos.fixture.ProductFixture.강정치킨;

public class MenuProductFixture {

    public static final MenuProduct 강정치킨_두마리 = create(1L, 강정치킨.getId(), 2);

    private MenuProductFixture() {
        throw new UnsupportedOperationException();
    }

    public static MenuProduct create(Long seq, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }
}
