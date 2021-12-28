package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {

    @DisplayName("메뉴 상품 생성 성공 테스트")
    @Test
    void instantiate_success() {
        // given
        Long productId = 1L;
        Quantity quantity = Quantity.of(2);

        // when
        MenuProduct menuProduct = MenuProduct.of(productId, quantity);

        // then
        assertAll(
                () -> assertThat(menuProduct).isNotNull()
                , () -> assertThat(menuProduct.getProductId()).isEqualTo(productId)
                , () -> assertThat(menuProduct.getQuantity()).isEqualTo(quantity)
        );
    }
}
