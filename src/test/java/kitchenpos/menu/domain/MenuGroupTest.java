package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.*;

@DisplayName("MenuGroup 클래스 테스트")
class MenuGroupTest {

    @DisplayName("MenuGroup를 생성한다.")
    @Test
    void successfulCreate() {
        MenuGroup menuGroup = new MenuGroup("신메뉴");
        assertThat(menuGroup).isNotNull();
    }

    @DisplayName("이름없이 MenuGroup을 생성한다.")
    @Test
    void failureCreateWithEmptyName() {
        assertThatThrownBy(() -> {
            new MenuGroup(null);
        }).isInstanceOf(NullPointerException.class);
    }
}
