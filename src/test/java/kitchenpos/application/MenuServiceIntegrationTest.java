package kitchenpos.application;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴에 상품을 여러개 등록할 수 있다.")
    @Test
    void createMenu() {
        Menu 후라이드_양념 = getMenu("후라이드+양념", 30000, new MenuProduct(1L, 1), new MenuProduct(2L, 1));
        Menu createdMenu = menuService.create(후라이드_양념);

        assertThat(createdMenu.getMenuProducts()).hasSize(2);
        assertThat(createdMenu.getPrice()).isEqualTo(new BigDecimal("30000.00"));
    }


    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void name() {
        assertThatThrownBy(() -> {
            Menu 후라이드_양념 = getMenu("후라이드+양념", -1, new MenuProduct(1L, 1), new MenuProduct(2L, 1));
            menuService.create(후라이드_양념);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴의 상품 가격 * 수량보다 작아야 한다.")
    @Test
    void existsMenuGroup() {
        assertThatThrownBy(() -> {
            Menu 두마리메뉴 = getMenu("두마리메뉴", 35000, new MenuProduct(1L, 1), new MenuProduct(2L, 1));
            menuService.create(두마리메뉴);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    public static Menu getMenu(String name, int price, MenuProduct... menuProducts) {
        return new Menu(name, new BigDecimal(price), 1L,
                Arrays.asList(menuProducts));
    }
}