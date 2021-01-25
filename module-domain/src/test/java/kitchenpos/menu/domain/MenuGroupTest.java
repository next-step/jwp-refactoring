package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MenuGroupTest {

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void createMenuGroup() {
        // given
        String name = "추천메뉴";

        // when
        MenuGroup menuGroup = new MenuGroup(name);

        // then
        assertThat(menuGroup).isNotNull();
    }

    @DisplayName("메뉴 그룹 이름은 필수 정보이다.")
    @Test
    void requireName() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            MenuGroup menuGroup = new MenuGroup("");
        }).withMessageMatching("메뉴 그룹 이름은 필수 정보입니다.");
    }
}
