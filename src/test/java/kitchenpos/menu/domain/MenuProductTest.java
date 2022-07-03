package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @DisplayName("메뉴상품을 생성한다.")
    @Test
    void create() {
        //when
        MenuProduct menuProduct = new MenuProduct(1L, 1L);

        //then
        assertAll(
                () -> assertThat(menuProduct).isNotNull(),
                () -> assertThat(menuProduct.getProductId()).isEqualTo(1L)
        );
    }

    @DisplayName("수량이 0 미만인 메뉴상품은 생성에 실패한다.")
    @Test
    void create_invalidQuantity() {
        //when & then
        assertThatThrownBy(() -> new MenuProduct(1L, -1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("수량 0 이상이어야 합니다.");
    }

}
