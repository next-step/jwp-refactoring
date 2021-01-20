package kitchenpos.menu;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @DisplayName("메뉴 그룹 등록")
    @Test
    void createMenuGroup() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("인기메뉴");

        MenuGroup result = menuGroupService.create(menuGroup);

        assertThat(result.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 조회")
    @Test
    void findMenuGroup() {
        List<MenuGroup> result = menuGroupService.list();

        assertThat(result).isNotEmpty();
    }

    @DisplayName("메뉴 등록")
    @Test
    void createMenu() {
        Menu menu = setUpMenu("순대국 + 치킨", new BigDecimal(25000));

        Menu result = menuService.create(menu);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getMenuProducts()).hasSize(1);
    }

    @DisplayName("메뉴 등록 실패")
    @Test
    void createMenuFail() {
        Menu menu = setUpMenu("순대국 + 치킨", new BigDecimal(-25000));

        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 조회")
    @Test
    void findMenu() {
        List<Menu> result = menuService.list();

        assertThat(result).isNotEmpty();
        assertThat(result).extracting("menuProducts").isNotEmpty();
    }

    private Menu setUpMenu(String name, BigDecimal price) {
        MenuGroup menuGroup = menuGroupService.list().get(0);
        Product product = productService.list().get(0);
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroup.getId());
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2L);
        menu.setMenuProducts(Arrays.asList(menuProduct));
        return menu;
    }
}
