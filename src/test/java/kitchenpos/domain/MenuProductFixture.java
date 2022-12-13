package kitchenpos.domain;

public class MenuProductFixture {
    private MenuProductFixture() {
    }

    public static MenuProduct menuProductParam(Long productId, long quantity) {
        return new MenuProduct(null, productId, quantity);
    }

    public static MenuProduct savedMenuProduct(Long id, MenuProduct param) {
        return new MenuProduct(id, param.getProductId(), param.getQuantity());
    }

    public static MenuProduct savedMenuProduct(Long id, Long productId, long quantity) {
        return new MenuProduct(id, productId, quantity);
    }
}
