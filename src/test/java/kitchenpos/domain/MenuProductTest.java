package kitchenpos.domain;

public class MenuProductTest {

    public static MenuProduct 메뉴_상품_생성(Menu menu, Product product, long quantity) {
        return new MenuProduct(menu, product, quantity);
    }
}
