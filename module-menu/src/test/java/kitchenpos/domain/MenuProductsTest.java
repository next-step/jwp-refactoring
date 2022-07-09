package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductsTest {
    private static Product 불고기버거 = new Product(1L, "불고기버거", BigDecimal.valueOf(1_500));@Test
    @DisplayName("메뉴 상품 추가")
    void isPossibleCreate() {
        // given
        final MenuProduct 불고기버거_상품 = MenuProduct.of(불고기버거.getId(), 5L);
        final MenuProducts menuProducts = new MenuProducts();
        // when
        menuProducts.add(불고기버거_상품);
        // then
        assertThat(menuProducts.getProducts()).hasSize(1);
    }

}
