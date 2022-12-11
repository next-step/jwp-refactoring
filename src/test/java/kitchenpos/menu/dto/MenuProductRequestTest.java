package kitchenpos.menu.dto;

public class MenuProductRequestTest {

    public static MenuProductRequest 메뉴상품_생성_요청_객체_생성(Long productId, long quantity) {
        return new MenuProductRequest.Builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}