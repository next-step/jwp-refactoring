package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.menu.application.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest extends BaseTest {
    private List<Menu> menus;

    @Autowired
    private MenuService menuService;

    @BeforeEach
    public void setUp() {
        super.setUp();
        menus = menuService.list();
    }

    @DisplayName("메뉴 등록")
    @Test
    void create() {
        Menu expected = menus.get(0);

        Menu actual = menuService.create(expected);

        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getMenuGroupId()).isEqualTo(expected.getMenuGroupId());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
    }

    @DisplayName("없는 메뉴 그룹인 경우 예외")
    @Test
    void validExistMenuGroup() {
        Menu expected = menus.get(0);
        expected.setMenuGroupId(10L);

        assertThatThrownBy(() -> {
            menuService.create(expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 없는 경우 예외")
    @Test
    void validMenuPrice() {
        Menu expected = menus.get(0);
        expected.setPrice(null);

        assertThatThrownBy(() -> {
            menuService.create(expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 메뉴별 상품의 총 가격보다 클 경우")
    @Test
    void validMenuPrice2() {
        Menu expected = menus.get(0);
        expected.setPrice(new BigDecimal(100000000));

        assertThatThrownBy(() -> {
            menuService.create(expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}