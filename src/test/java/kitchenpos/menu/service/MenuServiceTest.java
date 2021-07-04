package kitchenpos.menu.service;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("메뉴를 생성 한다")
    public void createMenu() {
        //given
        String name = "후라이드치킨";
        BigDecimal price = new BigDecimal(16000);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1L, 1L));
        Menu menu = new Menu(name, price, 1L, menuProducts);

        //when
        Menu createMenu = menuService.create(menu);

        //then
        assertThat(createMenu.getName()).isEqualTo(name);
        assertThat(createMenu.getPrice().compareTo(price)).isSameAs(0);
        assertThat(createMenu.getMenuGroupId()).isEqualTo(1L);
    }


    @Test
    @DisplayName("메뉴 생성 실패 - 가격이 음수")
    public void createMenuFailByPriceMinus() {
        //given
        String name = "불고기피자";
        BigDecimal price = new BigDecimal(-10000);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1L, 1L));
        Menu menu = new Menu(name, price, 1L, menuProducts);

        //when
        //then
        assertThrows(IllegalArgumentException.class, () ->  menuService.create(menu));
    }

    @Test
    @DisplayName("메뉴 리스트를 가져온다")
    public void selectMenuList() {
        //when
        List<Menu> menus = menuService.list();

        //then
        for (Menu menu : menus) {
            assertThat(menu.getId()).isNotNull();
            assertThat(menu.getName()).isNotNull();
            assertThat(menu.getPrice()).isNotNull();
            assertThat(menu.getMenuProducts()).isNotEmpty();
        }
    }
}
