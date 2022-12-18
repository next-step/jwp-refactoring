package kitchenpos.menu.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestExecutionListeners;

import java.math.BigDecimal;
import java.util.Arrays;

@DisplayName("메뉴 테스트")
class MenuTest {

    @DisplayName("메뉴 생성 시 가격은 음수가 나올 수 없다.")
    @Test
    void menu_price_not_negative_number() {
        // given && when && then
        Assertions.assertThatThrownBy(() -> new Menu("name", BigDecimal.valueOf(-1000), new MenuGroup("menuGroup")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 시 메뉴 그룹이 없으면 등록할 수 없다")
    @Test
    void empty_menu_group_not_create() {
        // given && when && then
        Assertions.assertThatThrownBy(() -> new Menu("name", BigDecimal.valueOf(1000), null))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
