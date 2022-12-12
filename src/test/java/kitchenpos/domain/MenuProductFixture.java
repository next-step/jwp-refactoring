package kitchenpos.domain;

public class MenuProductFixture {
    private MenuProductFixture() {
    }

    public static MenuProduct menuProductParam(Long productId, long quantity) {
        return new MenuProduct(null, null, productId, quantity);
    }

    public static MenuProduct savedMenuProduct(Long id, Long menuId, MenuProduct param) {
        return new MenuProduct(id, menuId, param.getProductId(), param.getQuantity());
    }

    public static MenuProduct savedMenuProduct(Long id, Long menuId, Long productId, long quantity) {
        return new MenuProduct(id, menuId, productId, quantity);
    }
}
