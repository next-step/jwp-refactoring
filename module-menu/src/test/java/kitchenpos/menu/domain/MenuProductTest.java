package kitchenpos.menu.domain;

import kitchenpos.core.exception.InvalidQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MenuProduct 클래스 테스트")
class MenuProductTest {

    private Long productId = 1L;

    @DisplayName("MenuProduct 생성한다.")
    @Test
    void successfulCreate() {
        MenuProduct menuProduct = new MenuProduct(productId, 1);
        assertThat(menuProduct).isNotNull();
    }

    @DisplayName("Product없이 MenuProduct 생성한다.")
    @Test
    void failureCreateWithEmptyProduct() {
        assertThatThrownBy(() -> {
            new MenuProduct(null, 1);
        }).isInstanceOf(NullPointerException.class);
    }

    @DisplayName("Product없이 MenuProduct 생성한다.")
    @Test
    void failureCreateWithNegativeQuantity() {
        assertThatThrownBy(() -> {
            new MenuProduct(productId, -1);
        }).isInstanceOf(InvalidQuantityException.class);
    }
}
