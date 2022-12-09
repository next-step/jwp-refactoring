package kitchenpos.domain;

public class MenuProductTestFixture {
    public static final MenuProduct 짜장면_1그릇 = menuProduct(1L, null, 1L, 1);

    public static MenuProduct menuProduct(Long seq, Long menuId, Long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
