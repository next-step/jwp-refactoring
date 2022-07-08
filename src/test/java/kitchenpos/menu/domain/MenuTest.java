package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;

class MenuTest {
    private MenuGroup 추천_메뉴;
    private Product 후라이드_치킨;
    private MenuProduct 메뉴_후라이드_치킨;

    @BeforeEach
    void init() {
        // given
        추천_메뉴 = new MenuGroup(1L, "추천메뉴");
        후라이드_치킨 = new Product(1L, "후라이드치킨", new BigDecimal(8_500L));
        메뉴_후라이드_치킨 = new MenuProduct(후라이드_치킨.getId(),2);
    }
}
