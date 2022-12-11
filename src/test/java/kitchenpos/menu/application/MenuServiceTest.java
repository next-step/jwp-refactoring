package kitchenpos.menu.application;

import com.navercorp.fixturemonkey.FixtureMonkey;
import kitchenpos.menu.persistence.MenuDao;
import kitchenpos.menu.persistence.MenuGroupDao;
import kitchenpos.menu.persistence.MenuProductDao;
import kitchenpos.product.persistence.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @InjectMocks
    private MenuService menuService;
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    public static FixtureMonkey fixtureMonkey;

    @BeforeAll
    public static void setup() {
        fixtureMonkey = FixtureMonkey.create();
    }

    @DisplayName("메뉴가격이 없는경우 예외발생")
    @Test
    public void throwsExceptionWhenNullPrice() {
        assertThatThrownBy(() -> menuService.create(new Menu()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가격이 0보다 작은경우 예외발생")
    @Test
    public void throwsExceptionWhenNegativePrice() {
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(Arbitraries.integers().lessOrEqual(-1).sample()));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 속한 메뉴그룹이 없는경우 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsMeneGroup() {
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(15000));
        menu.setMenuGroupId(Arbitraries.longs().sample());
        doReturn(false).when(menuGroupDao).existsById(menu.getMenuGroupId());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 구성하는 상품이 없는경우 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsProduct() {
        List<MenuProduct> mockProducts = fixtureMonkey.giveMeBuilder(MenuProduct.class)
                .set("productId", Arbitraries.longs().between(1, 1000))
                .sampleList(5);
        Menu menu = fixtureMonkey
                .giveMeBuilder(Menu.class)
                .set("id", Arbitraries.longs().between(1, 100))
                .set("price", BigDecimal.valueOf(15000))
                .set("menuGroupId", Arbitraries.longs().between(1, 50))
                .set("menuProducts", mockProducts)
                .sample();
        doReturn(true).when(menuGroupDao).existsById(menu.getMenuGroupId());
        doReturn(Optional.empty()).when(productDao).findById(anyLong());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가격이 메뉴구성상품들의 총 가격 높은경우 예외발생")
    @Test
    public void throwsExceptionWhenMenuPriceGreater() {
        Product product = fixtureMonkey.giveMeBuilder(Product.class)
                .set("price", BigDecimal.valueOf(Arbitraries.integers().between(1000, 1500).sample()))
                .sample();
        List<MenuProduct> menuProduct = fixtureMonkey.giveMeBuilder(MenuProduct.class)
                .set("quantity", Arbitraries.integers().between(1, 5))
                .set("productId", 13l)
                .sampleList(5);
        Menu menu = fixtureMonkey
                .giveMeBuilder(Menu.class)
                .set("id", Arbitraries.longs().between(1, 100))
                .set("price", BigDecimal.valueOf(200000))
                .set("menuGroupId", Arbitraries.longs().between(1, 50))
                .set("menuProducts", menuProduct)
                .sample();
        doReturn(true)
                .when(menuGroupDao)
                .existsById(menu.getMenuGroupId());
        doReturn(Optional.ofNullable(product))
                .when(productDao)
                .findById(anyLong());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴룰 추가하면 메뉴정보를 반환")
    @Test
    public void returnMenu() {
        Product product = fixtureMonkey.giveMeBuilder(Product.class)
                .set("price", BigDecimal.valueOf(Arbitraries.integers().between(1000, 1500).sample()))
                .sample();
        MenuProduct menuProduct = fixtureMonkey.giveMeBuilder(MenuProduct.class)
                .set("menuId", Arbitraries.longs().between(1, 5))
                .set("quantity", Arbitraries.integers().between(1, 5))
                .set("productId", 13l)
                .sample();
        List<MenuProduct> menuProducts = fixtureMonkey.giveMeBuilder(MenuProduct.class)
                .set("quantity", Arbitraries.integers().between(1, 5))
                .set("productId", 13l)
                .sampleList(5);
        Menu savedMenu = fixtureMonkey.giveMeBuilder(Menu.class)
                .set("id", Arbitraries.longs().between(1, 5))
                .set("price", BigDecimal.valueOf(0l))
                .set("menuGroupId", 15l)
                .set("menuProducts", menuProducts)
                .sample();
        Menu menu = fixtureMonkey
                .giveMeBuilder(Menu.class)
                .set("id", Arbitraries.longs().between(1, 100))
                .set("price", BigDecimal.valueOf(0))
                .set("menuGroupId", Arbitraries.longs().between(1, 50))
                .set("menuProducts", menuProducts)
                .sample();
        doReturn(true)
                .when(menuGroupDao)
                .existsById(menu.getMenuGroupId());
        doReturn(Optional.ofNullable(product))
                .when(productDao)
                .findById(anyLong());
        doReturn(savedMenu)
                .when(menuDao)
                .save(menu);
        doReturn(menuProduct)
                .when(menuProductDao)
                .save(any(MenuProduct.class));

        Menu returnedMenu = menuService.create(menu);

        assertAll(
                () -> assertThat(returnedMenu.getId()).isBetween(1l, 5l),
                () -> assertThat(returnedMenu.getMenuGroupId()).isEqualTo(15l),
                () -> assertThat(returnedMenu.getMenuProducts().stream().map(MenuProduct::getMenuId).collect(Collectors.toList()))
                        .allMatch(menuId -> menuId >= 1 && menuId <= 5),
                () -> assertThat(returnedMenu.getPrice()).isEqualTo(BigDecimal.valueOf(0)));
    }

    @DisplayName("메뉴목록을 조회하는경우 메뉴목록을 반환")
    @Test
    public void returnMenus() {
        List<Menu> menus = fixtureMonkey
                .giveMeBuilder(Menu.class)
                .set("id",Arbitraries.longs().between(1,1000l))
                .setNull("menuProducts")
                .sampleList(5);
        List<MenuProduct> menuProducts = fixtureMonkey
                .giveMeBuilder(MenuProduct.class)
                .sampleList(5);
        doReturn(menus)
                .when(menuDao)
                .findAll();
        doReturn(menuProducts)
                .when(menuProductDao)
                .findAllByMenuId(anyLong());

        List<Menu> returnedMenus = menuService.list();

        List<MenuProduct> returnedMenuProducts = returnedMenus.stream()
                .flatMap(menu -> menu.getMenuProducts().stream())
                .collect(Collectors.toList());
        assertThat(returnedMenuProducts).containsAll(menuProducts);
    }

}
