package kitchenpos.domain.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuTest {
    private static final Long ANY_MENU_GROUP_ID = 1L;
    private Menu menu;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.of("menuGroupName");
        ReflectionTestUtils.setField(menuGroup, "id", ANY_MENU_GROUP_ID);
        menu = Menu.of("tomato pasta", Price.of(0L), menuGroup);
    }

    @Test
    @DisplayName("메뉴는 이름, 가격, 메뉴 그룹 그리고 0개 이상의 메뉴 상품으로 구성된다.")
    void menu_create() {
        assertThat(menu.getName()).isNotNull();
        assertThat(menu.getPrice()).isNotNull();
        assertThat(menu.getMenuGroup()).isNotNull();

        MenuProducts menuProducts = menu.getMenuProducts();
        assertThat(menuProducts).isNotNull();
    }
}