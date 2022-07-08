package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품")
class MenuProductTest {
    private static final MenuProduct 메뉴_상품 = MenuProduct.of(1L, 3L);

    @DisplayName("생성")
    @Nested
    class 생성 {
        @DisplayName("상품, 수량을 지정하면 생성할 수 있다.")
        @Test
        void 생성_성공() {
            assertThat(메뉴_상품).isNotNull();
        }

        @DisplayName("상품이 NULL이면 생성할 수 없습니다.")
        @Test
        void 상품이_NULL() {
            assertThatThrownBy(() -> MenuProduct.of(null, 3L)).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
