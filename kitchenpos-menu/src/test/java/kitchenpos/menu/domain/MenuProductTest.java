package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MenuProduct 클래스 테스트")
class MenuProductTest {
    private Long productId = 1L;

    @DisplayName("MenuProduct 생성한다.")
    @Test
    void createMenuProduct() {
        MenuProduct menuProduct = new MenuProduct(productId, 1);
        assertThat(menuProduct).isNotNull();
    }
}
