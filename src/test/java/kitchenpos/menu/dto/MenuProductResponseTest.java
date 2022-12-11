package kitchenpos.menu.dto;

public class MenuProductResponseTest {

    public static MenuProductResponse 메뉴상품_응답_객체_생성(Long seq, Long menuId, Long productId, long quantity) {
        return new MenuProductResponse.Builder()
                .seq(seq)
                .menuId(menuId)
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}