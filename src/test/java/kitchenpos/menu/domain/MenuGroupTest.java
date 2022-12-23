package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("MenuGroup 클래스 테스트")
public class MenuGroupTest {

    @DisplayName("MenuGroup을 생성한다")
    @Test
    void MenuGroup_생성() {
        // when
        MenuGroup menuGroup = new MenuGroup("오일");

        // then
        assertThat(menuGroup.getName()).isEqualTo("오일");
    }
}
