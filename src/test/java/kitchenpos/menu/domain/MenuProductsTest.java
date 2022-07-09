package kitchenpos.menu.domain;

import kitchenpos.product.domain.ProductTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductsTest {
    @Test
    @DisplayName("메뉴 상품 추가")
    void isPossibleCreate() {
        // given
        final MenuProduct 불고기버거 = MenuProduct.of(ProductTest.불고기버거.getId(), 5L);
        final MenuProducts menuProducts = new MenuProducts();
        // when
        menuProducts.add(불고기버거);
        // then
        assertThat(menuProducts.getProducts()).hasSize(1);
    }

}
