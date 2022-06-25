package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품이 같은지 검증")
    void verifyEqualsMenuProduct() {
        final MenuProductV2 menuProduct = new MenuProductV2(1L, 1L, 1L, 1L);

        assertThat(menuProduct).isEqualTo(new MenuProductV2(1L, 1L, 1L, 1L));
    }
}
