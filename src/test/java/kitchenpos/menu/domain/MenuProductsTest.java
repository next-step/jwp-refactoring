package kitchenpos.menu.domain;

import kitchenpos.product.domain.ProductTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductsTest {
    public static MenuProducts 메뉴_제품 = MenuProducts.of(
            Arrays.asList(MenuProduct.of(ProductTest.불고기버거, 5L), MenuProduct.of(ProductTest.새우버거, 1L)));

    /**
     * 메뉴 생성 시 모든 제품의 합보다 금액은 작아야 한다.
     */
    @Test
    @DisplayName("메뉴 생성 가능 유무")
    void isPossibleCreate() {
        // when
        boolean possible = 메뉴_제품.isPossibleCreate(BigDecimal.valueOf(8_000));
        // then
        assertThat(possible).isTrue();
    }

}
