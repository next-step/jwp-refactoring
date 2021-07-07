package kitchenpos.tobe.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MenuTest {

    @DisplayName("메뉴를 생성하고 생성자의 값을 리턴한다.")
    @Test
    void create() {
        Menu menu = Menu.builder()
                .id(1L)
                .menuGroupId(1L)
                .menuProducts(new MenuProducts())
                .name("후라이드+후라이드")
                .price(BigDecimal.valueOf(19000))
                .builder();

        assertThat(menu).isNotNull();
    }
}
