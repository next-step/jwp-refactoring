package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupTest {
    public static final MenuGroup 햄버거_메뉴 = new MenuGroup(1L, "햄버거_메뉴");

    @Test
    @DisplayName("메뉴 그룹 생성")
    void create() {
        // then
        assertThat(햄버거_메뉴).isInstanceOf(MenuGroup.class);
    }
}
