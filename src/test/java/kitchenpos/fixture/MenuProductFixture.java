package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품_후라이드_치킨 = create(2L, ProductFixture.후라이드치킨.getId(), 1L);
    public static MenuProduct 메뉴_상품_강정_치킨 = create(1L, ProductFixture.강정치킨.getId(), 1L);

    public static MenuProduct create(Long seq, Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

}
