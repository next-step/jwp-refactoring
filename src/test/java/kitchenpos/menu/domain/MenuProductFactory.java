package kitchenpos.menu.domain;

public class MenuProductFactory {

    public static MenuProduct create(Long seq, Menu menu, long productId, long quantity) {
        return new MenuProduct(seq, menu, productId, quantity);
    }
}
