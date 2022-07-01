package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품에 대한 단위 테스트")
class MenuProductTest {

    @DisplayName("메뉴 상품에 메뉴 객체를 매핑하면 정상적으로 매핑되어야 한다")
    @Test
    void mapping_test() {
        // given
        MenuProduct menuProduct = MenuProduct.of(1L, 3);

        // when
        Menu menu = Menu.of("menu", BigDecimal.valueOf(500L), null, Collections.singletonList(menuProduct));

        // then
        assertNotNull(menuProduct.getMenu());
        assertThat(menuProduct.getMenu()).isEqualTo(menu);
    }
}
