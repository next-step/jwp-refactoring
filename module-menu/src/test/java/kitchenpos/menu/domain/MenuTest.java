package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴 금액은 0원 이상이다")
    void saveMenuProducts() {
        // given
        BigDecimal price = BigDecimal.ONE;

        // when
        Menu menu = new Menu("라면", price, 1L);

        // then
        assertThat(menu.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("메뉴 금액이 0원 미만인 경우 오류가 발생한다")
    void saveMenuProducts_error() {
        // given & when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Menu("라면", BigDecimal.valueOf(-1), 1L)
        ).withMessageContaining("유효하지 않은 금액입니다.");
    }
}
