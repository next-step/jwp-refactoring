package kitchenpos.domain;

public class MenuProductFixture {
    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId, int quantity) {
        return new MenuProduct(seq, menuId, productId, quantity);
    }

    public static MenuProduct createMenuProduct(Long productId, int quantity) {
        return new MenuProduct(null, null, productId, quantity);
    }
}
