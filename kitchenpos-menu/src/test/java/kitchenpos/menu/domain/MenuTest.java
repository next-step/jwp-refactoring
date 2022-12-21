package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuTest {

    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup("한가지메뉴");
        menuProducts = Arrays.asList(
                MenuProduct.of(1L, 1L),
                MenuProduct.of(2L, 1L)
        );
    }

    @Test
    @DisplayName("메뉴 생성에 성공한다")
    void createMenuTest() {
        // when
        Menu newMenu = Menu.of("후라이드치킨", 17_000L, 1L, menuProducts);

        // then
        assertThat(newMenu).isEqualTo(Menu.of("후라이드치킨", 17_000L, 1L, menuProducts));
    }
}
