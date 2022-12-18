package kitchenpos.menu.dto;

import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponseTest {

    public static MenuProductResponse 메뉴상품_응답_객체_생성(Long seq, long quantity, ProductResponse product) {
        return new MenuProductResponse.Builder()
                .seq(seq)
                .quantity(quantity)
                .product(product)
                .build();
    }
}