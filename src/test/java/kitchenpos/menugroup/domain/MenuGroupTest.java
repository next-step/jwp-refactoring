package kitchenpos.menugroup.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹")
class MenuGroupTest {
    @DisplayName("이름을 지정하면 생성할 수 있다.")
    @Test
    void 생성_성공() {
        assertThat(new MenuGroup("메뉴 그룹")).isNotNull();
    }

    @DisplayName("이름이 NULL이면 생성할 수 없습니다.")
    @Test
    void 이름이_NULL() {
        assertThatThrownBy(() -> new MenuGroup(null)).isInstanceOf(IllegalArgumentException.class);
    }
}
