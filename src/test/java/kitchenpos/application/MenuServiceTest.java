package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    private MenuGroup menuGroup;
    private MenuProduct pizza;
    private MenuProduct pasta;

    @BeforeEach
    void setup() {
        menuGroup = createMenuGroup("양식 메뉴 그룹");
        pizza = createMenuProduct("피자", BigDecimal.ONE, 1);
        pasta = createMenuProduct("파스타", BigDecimal.ONE, 1);
    }

    @Test
    void 생성시_메뉴가격이_0보다작을경우_예외발생() {
        final BigDecimal menuPrice = BigDecimal.valueOf(-1);
        final Menu menu = createMenu("점심메뉴", menuPrice, menuGroup, Arrays.asList(pizza, pasta));
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성시_메뉴그룹이존재하지않을경우_예외발생() {
        final MenuGroup nonExistentMenuGroup = new MenuGroup("새로운 메뉴 그룹");
        final Menu menu = createMenu("점심메뉴", BigDecimal.ONE, nonExistentMenuGroup, Arrays.asList(pizza, pasta));
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성시_메뉴상품이존재하지않을경우_예외발생() {
        final MenuProduct nonExistentMenuProduct = new MenuProduct(-1L, 1);
        final Menu menu = createMenu("점심메뉴", BigDecimal.ONE, menuGroup, Arrays.asList(nonExistentMenuProduct, pasta));
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성시_메뉴가격이상품의합보다클경우_예외발생() {
        final BigDecimal menuPrice = BigDecimal.TEN;
        final Menu menu = createMenu("점심메뉴", menuPrice, menuGroup, Arrays.asList(pizza, pasta));
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성시_정상적인메뉴의경우_생성한메뉴반환() {
        final Menu menu = menuService.create(
            createMenu("점심메뉴", BigDecimal.ONE, menuGroup, Arrays.asList(pizza, pasta)));
        assertAll(
            () -> assertThat(menu.getName()).isEqualTo("점심메뉴"),
            () -> assertThat(menu.getPrice()).isEqualTo(new BigDecimal("1.00")),
            () -> assertThat(menu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
            () -> assertMenuProducts(menu.getMenuProducts(), Arrays.asList(pizza, pasta))
        );
    }

    @Test
    void 조회시_존재하는메뉴목록반환() {
        assertThat(menuService.list()).isNotEmpty();
    }


    private void assertMenuProducts(List<MenuProduct> actual, List<MenuProduct> expected) {
        actual.stream().map(MenuProduct::getQuantity).collect(Collectors.toList());
        assertAll(
            () -> assertThat(actual).hasSize(expected.size()),
            () -> {
                for (int i = 0; i < actual.size(); i++) {
                    assertMenuProduct(actual.get(i), expected.get(i));
                }
            }
        );
    }

    private void assertMenuProduct(MenuProduct actualMenuProduct, MenuProduct expectedMenuProduct) {
        assertAll(
            () -> assertThat(actualMenuProduct.getProductId()).isEqualTo(expectedMenuProduct.getProductId()),
            () -> assertThat(actualMenuProduct.getQuantity()).isEqualTo(expectedMenuProduct.getQuantity())
        );
    }

    private MenuGroup createMenuGroup(String name) {
        return menuGroupDao.save(new MenuGroup(name));
    }

    private Menu createMenu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroup.getId(), menuProducts);
    }

    private MenuProduct createMenuProduct(String name, BigDecimal price, long quantity) {
        final Product product = productDao.save(new Product(name, price));
        return new MenuProduct(product.getId(), quantity);
    }
}
