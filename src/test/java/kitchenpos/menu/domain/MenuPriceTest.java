package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 가격 테스트")
class MenuPriceTest {

    @DisplayName("동등성")
    @Test
    void equals() {
        assertThat(MenuPrice.from(10000L)).isEqualTo(MenuPrice.from(10000L));
        assertThat(MenuPrice.from(10000L)).isNotEqualTo(MenuPrice.from(12000L));
    }

    @DisplayName("가격 없음")
    @Test
    void validate_price_null() {
        assertThatThrownBy(() -> MenuPrice.from(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 0원")
    @Test
    void validate_price_zero() {
        assertThatThrownBy(() -> MenuPrice.from(0L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
