package kitchenpos.menugroup.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹 테스트")
class MenuGroupTest {

    @DisplayName("id가 같은 두 객체는 동등하다.")
    @Test
    void equalsTest() {
        MenuGroup group1 = MenuGroup.of(1L, "group1");
        MenuGroup group2 = MenuGroup.of(1L, "group1");

        Assertions.assertThat(group1).isEqualTo(group2);
    }

    @DisplayName("id가 다르면 두 객체는 동등하지 않다.")
    @Test
    void equalsTest2() {
        MenuGroup group1 = MenuGroup.of(1L, "group1");
        MenuGroup group2 = MenuGroup.of(2L, "group1");

        Assertions.assertThat(group1).isNotEqualTo(group2);
    }
}
