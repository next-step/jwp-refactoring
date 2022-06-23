package kitchenpos.menu;

import kitchenpos.menu.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuRequestTest {
    @Test
    @DisplayName("메뉴 가격이 음수면 요청을 생성할 수 없다")
    void create_failed_1() {
        assertThatThrownBy(
                () -> new MenuRequest("name", BigDecimal.valueOf(-1), 0L, Collections.emptyList())).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 이름이 비어있으면 요청을 생성할 수 없다")
    void create_failed_2() {
        assertThatThrownBy(() -> new MenuRequest(null, BigDecimal.valueOf(10000), 0L,
                Collections.emptyList())).isExactlyInstanceOf(IllegalArgumentException.class);
    }

}
