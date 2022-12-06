package kitchenpos.domain;

public class MenuProductTestFixture {

    public static MenuProduct generateMenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        return MenuProduct.of(seq, menuId, productId, quantity);
    }
}
