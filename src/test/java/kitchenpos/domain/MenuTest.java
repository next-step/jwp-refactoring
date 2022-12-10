package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MenuTest {
    @Test
    public void 생성() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드");
        menu.setPrice(BigDecimal.valueOf(16000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(new MenuProduct()));

        assertAll(
                () -> assertThat(menu.getId()).isEqualTo(1L),
                () -> assertThat(menu.getName()).isEqualTo("후라이드"),
                () -> assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(16000)),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(1L),
                () -> assertThat(menu.getMenuProducts().size()).isEqualTo(1)
        );
    }
}
