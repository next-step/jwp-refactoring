package kitchenpos.menugroup.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupTest {

    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup(1L, "추천메뉴");

        assertThat(menuGroup).isNotNull();
    }
}
