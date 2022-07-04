package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {
    private Product 후라이드_치킨 = new Product("후라이드치킨", new BigDecimal(8_500L));
    private Product 감자_튀김 = new Product("감자튀김", new BigDecimal(3_000L));

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
