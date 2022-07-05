package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 단위테스트")
class MenuTest {
    @DisplayName("메뉴의 가격은 0원 이상이어야 한다")
    @Test
    void priceMoreThen0() {
        assertThatThrownBy(() -> new Menu("세트1", -1, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
