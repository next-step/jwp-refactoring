package kitchenpos.application;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.application.MenuServiceIntegrationTest.getMenu;
import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceIntegrationTest extends IntegrationTest {

    @Autowired
    MenuService menuService;

    @DisplayName("메뉴 그룹에 여러개의 메뉴를 등록할 수 있다.")
    @Test
    void addMenus() {
        Menu menu = menuService.create(getMenu("후라이드+양념", 30000, new MenuProduct(1L, 1), new MenuProduct(2L, 1)));

        assertThat(menu.getMenuProducts()).hasSize(2);
    }

    @DisplayName("매뉴의 목록을 조회할 수 있다.")
    @Test
    void listMenu() {
        List<Menu> menus = menuService.list();
        assertThat(menus)
                .extracting("name")
                .containsExactly("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
    }
}