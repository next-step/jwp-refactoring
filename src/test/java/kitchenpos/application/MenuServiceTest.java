package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-10
 */
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴 생성시 금액이 0원 아래일 경우")
    @Test
    void menuCreateWithMinusPriceTest() {
        Menu menu = getMenu();
        menu.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성시 메뉴 그룹이 존재하지 않을 경우")
    @Test
    void menuCreateWithoutMenuGroup() {
        Menu menu = getMenu();
        menu.setMenuGroupId(20L);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성시 메뉴의 총 합이 안맞는 경우")
    @Test
    void menuCreateWithWrongAmountTotalSum() {
        Menu menu = getMenu();
        menu.getMenuProducts().get(0).setProductId(1L);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }


    private Menu getMenu() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(6L);
        menuProduct.setQuantity(1);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);

        Menu menu = new Menu();
        menu.setName("메뉴");
        menu.setPrice(BigDecimal.valueOf(17_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(menuProducts);

        return menu;
    }
}
