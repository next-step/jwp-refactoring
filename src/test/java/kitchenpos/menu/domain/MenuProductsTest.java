package kitchenpos.menu.domain;

import static kitchenpos.product.domain.ProductTest.상품_생성;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;

class MenuProductsTest {
    private Product 후라이드_치킨;
    private Product 감자_튀김;

    @BeforeEach
    void init() {
        후라이드_치킨 = 상품_생성("후라이드치킨", 8_500L);
        감자_튀김 = 상품_생성("감자튀김", 3_000L);
    }
}
