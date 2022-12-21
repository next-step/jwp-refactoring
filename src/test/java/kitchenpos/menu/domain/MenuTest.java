package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.Test;

class MenuTest {
    @Test
    public void 생성() {
        Menu menu = new Menu(1L, "후라이드", BigDecimal.valueOf(16000), 1L);

        assertAll(
                () -> assertThat(menu.getId()).isEqualTo(1L),
                () -> assertThat(menu.getName()).isEqualTo("후라이드"),
                () -> assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(16000)),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(1L)
        );
    }
}
