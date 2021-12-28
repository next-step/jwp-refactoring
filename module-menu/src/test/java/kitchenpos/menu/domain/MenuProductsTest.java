package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 상품들 테스트")
class MenuProductsTest {

    @DisplayName("메뉴 상품들 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // given
        Long productId = 1L;
        MenuProduct menuProduct = MenuProduct.of(productId, Quantity.of(2));

        // when
        MenuProducts menuProducts = MenuProducts.of(Arrays.asList(menuProduct));

        // then
        assertAll(
                () -> assertThat(menuProducts).isNotNull()
                , () -> assertThat(menuProducts.getMenuProducts()).isEqualTo(Arrays.asList(menuProduct))
        );
    }
}
