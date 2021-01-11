package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuServiceIntegrationTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴에 상품을 여러개 등록할 수 있다.")
    @Test
    void createMenu() {
        Menu 후라이드_양념 = new Menu("후라이드+양념", new BigDecimal(30000), 1L,
                getMenuProducts(new MenuProduct(1L, 1),new MenuProduct(2L, 1)));
        Menu createdMenu = menuService.create(후라이드_양념);

        assertThat(createdMenu.getMenuProducts()).hasSize(2);
        assertThat(createdMenu.getPrice()).isEqualTo(new BigDecimal("30000.00"));
    }

    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void name() {
        assertThatThrownBy(() -> {
            Menu 후라이드_양념 = new Menu("후라이드+양념", new BigDecimal(-1), 1L, getMenuProducts(
                    new MenuProduct(1L, 1),
                    new MenuProduct(2L, 1)
            ));
            menuService.create(후라이드_양념);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹에 여러개의 메뉴를 등록할 수 있다.")
    @Test
    void addMenus() {

        menuService.create(new Menu("후라이드+양념", new BigDecimal(30000), 1L, getMenuProducts(
                new MenuProduct(1L, 1),
                new MenuProduct(2L, 1)
        )));
        menuService.create(new Menu("양념+통구이", new BigDecimal(30000), 1L, getMenuProducts(
                new MenuProduct(2L, 1),
                new MenuProduct(4L, 1)
        )));

        List<Menu> list = menuService.list();
        assertThat(list.stream().filter(e -> e.getMenuGroupId().equals(1L))
                .collect(Collectors.toList())).hasSize(2);
    }

    private List<MenuProduct> getMenuProducts(MenuProduct... menuProducts) {
        return Arrays.asList(menuProducts);
    }

    @DisplayName("메뉴의 가격이 메뉴의 상품 가격 * 수량보다 작아야 한다.")
    @Test
    void existsMenuGroup() {
        assertThatThrownBy(() -> {
            Menu 두마리메뉴 = new Menu("두마리메뉴", new BigDecimal(35000), 1L, getMenuProducts(
                    new MenuProduct(1L, 1),
                    new MenuProduct(2L, 1)
            ));
            menuService.create(두마리메뉴);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("매뉴의 목록을 조회할 수 있다.")
    @Test
    void listMenu() {
        List<Menu> list = menuService.list();
        assertThat(list)
                .extracting("name")
                .containsExactly("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
    }


}