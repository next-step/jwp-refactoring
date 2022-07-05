package kitchenpos.menu.domain;

import static kitchenpos.product.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {
    private Product 후라이드_치킨;
    private Product 감자_튀김;

    @BeforeEach
    void init() {
        후라이드_치킨 = 상품_생성("후라이드치킨", 8_500L);
        감자_튀김 = 상품_생성("감자튀김", 3_000L);
    }

    @Test
    @DisplayName("메뉴 상품 총합을 계산한다.")
    void calculateTotalPrice() {
        // given
        MenuProduct 메뉴_후라이드_치킨 = new MenuProduct(후라이드_치킨, 2);
        MenuProduct 메뉴_감자_튀김 = new MenuProduct(감자_튀김, 1);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(메뉴_후라이드_치킨, 메뉴_감자_튀김));

        // when
        BigDecimal totalPrice = menuProducts.calculateTotalPrice();

        assertThat(totalPrice).isEqualTo(new BigDecimal(20_000));
    }
}
