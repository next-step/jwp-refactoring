package kitchenpos.menugroup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {
    @DisplayName("초기화 테스트")
    @Test
    void from() {
        MenuGroup menuGroup = MenuGroup.from(1L, "양념");
        assertThat(menuGroup).isEqualTo(menuGroup);
    }
}
