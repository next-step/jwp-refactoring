package kitchenpos.application.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {


    @DisplayName("메뉴를 구성하는 메뉴 상품들에 상품이 존재하지 않으면 등록할 수 없다.")
    @Test
    void validate() {
        // given && when && then
        assertThatThrownBy(() -> new MenuProduct(null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품을 등록해주세요.");
    }

}
