package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductTest {
    private static Product 불고기버거 = new Product(1L, "불고기버거", BigDecimal.valueOf(1_500));
    @Test
    @DisplayName("메뉴 상품 추가")
    void create() {
        // when
        final MenuProduct 불고기버거_상품 = MenuProduct.of(불고기버거.getId(), 5L);
        // then
        assertThat(불고기버거_상품).isInstanceOf(MenuProduct.class);
    }
}
