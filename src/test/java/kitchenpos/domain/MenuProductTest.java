package kitchenpos.domain;

public class MenuProductTest {

    public static MenuProduct 메뉴상품_생성(Long seq, Long menuId, Long productId, long quantity) {
        return new MenuProduct.Builder()
                .seq(seq)
                .menuId(menuId)
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}