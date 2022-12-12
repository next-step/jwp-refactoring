package kitchenpos.menu.application;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.generator.BuilderArbitraryGenerator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.persistence.MenuGroupRepository;
import kitchenpos.menu.persistence.MenuProductRepository;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.product.domain.Money;
import kitchenpos.product.domain.Product;
import kitchenpos.product.persistence.ProductRepository;
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
import java.util.stream.IntStream;

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
    private MenuRepository menuDao;
    @Mock
    private MenuGroupRepository menuGroupDao;
    @Mock
    private MenuProductRepository menuProductDao;
    @Mock
    private ProductRepository productDao;

    public static FixtureMonkey fixtureMonkey;

    @BeforeAll
    public static void setup() {
        fixtureMonkey = FixtureMonkey.builder()
                .defaultGenerator(BuilderArbitraryGenerator.INSTANCE)
                .build();
    }

    @DisplayName("메뉴가격이 없는경우 예외발생")
    @Test
    public void throwsExceptionWhenNullPrice() {
        assertThatThrownBy(() -> menuService.create(Menu.builder().build()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가격이 0보다 작은경우 예외발생")
    @Test
    public void throwsExceptionWhenNegativePrice() {
        Menu menu = Menu.builder()
                .price(BigDecimal.valueOf(Arbitraries.integers().lessOrEqual(-1).sample()))
                .build();

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 속한 메뉴그룹이 없는경우 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsMeneGroup() {
        Menu menu = Menu.builder()
                .price(BigDecimal.valueOf(15000))
                .menuGroup(MenuGroup.builder().id(Arbitraries.longs().between(1, 50).sample()).build())
                .build();
        doReturn(false).when(menuGroupDao).existsById(anyLong());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 구성하는 상품이 없는경우 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsProduct() {
        List<MenuProduct> menuProducts = getMenuProducts(MenuProduct.builder()
                .productId(Arbitraries.longs().between(1, 1000).sample())
                .menu(Menu.builder().build())
                .build(), 5);
        Menu menu = Menu.builder()
                        .id(Arbitraries.longs().between(1, 100).sample())
                        .price(BigDecimal.valueOf(15000))
                .menuGroup(MenuGroup.builder().id(Arbitraries.longs().between(1, 50).sample()).build())
                        .menuProducts(menuProducts)
                        .build();
        doReturn(true).when(menuGroupDao)
                .existsById(anyLong());
        doReturn(Optional.empty()).when(productDao).findById(anyLong());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가격이 메뉴구성상품들의 총 가격 높은경우 예외발생")
    @Test
    public void throwsExceptionWhenMenuPriceGreater() {
        Product product = fixtureMonkey
                .giveMeBuilder(Product.class)
                .set("money", Money.of(Arbitraries.longs().between(1000, 1500).sample()))
                .sample();
        List<MenuProduct> menuProduct = getMenuProducts(MenuProduct.builder()
                .seq(Arbitraries.longs().between(1, 10).sample())
                .productId(Arbitraries.longs().between(1, 20).sample())
                .quantity(Arbitraries.longs().between(1, 20).sample())
                .menu(Menu.builder().build())
                .build(), 5);
        Menu menu = Menu.builder()
                .id(Arbitraries.longs().between(1, 100).sample())
                .price(BigDecimal.valueOf(200000))
                .menuGroup(MenuGroup.builder().id(Arbitraries.longs().between(1, 50).sample()).build())
                .menuProducts(menuProduct)
                .build();
        doReturn(true)
                .when(menuGroupDao)
                .existsById(anyLong());
        doReturn(Optional.ofNullable(product))
                .when(productDao)
                .findById(anyLong());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴룰 추가하면 메뉴정보를 반환")
    @Test
    public void returnMenu() {
        Product product = Product.builder()
                .money(Money.of(2000l))
                .build();
        MenuProduct menuProduct =
                MenuProduct.builder()
                        .menu(Menu.builder().build())
                        .quantity(5l)
                        .productId(13l)
                        .build();
        List<MenuProduct> menuProducts =
                getMenuProducts(menuProduct, 5);
        Menu savedMenu = Menu.builder()
                .id(15l)
                .name("Pasta")
                .price(BigDecimal.valueOf(0))
                .menuGroup(MenuGroup.builder().id(Arbitraries.longs().between(1, 50).sample()).build())
                .menuProducts(menuProducts)
                .build();
        Menu menu = Menu.builder()
                .id(15l)
                .name("Pasta")
                .price(BigDecimal.valueOf(0))
                .menuGroup(MenuGroup.builder().id(Arbitraries.longs().between(1, 50).sample()).build())
                .menuProducts(menuProducts)
                .build();
        doReturn(true)
                .when(menuGroupDao)
                .existsById(anyLong());
        doReturn(Optional.ofNullable(product))
                .when(productDao)
                .findById(anyLong());
        doReturn(savedMenu)
                .when(menuDao)
                .save(menu);
        doReturn(menuProduct)
                .when(menuProductDao)
                .save(any(MenuProduct.class));

        MenuResponse returnedMenu = menuService.create(menu);

        assertAll(
                () -> assertThat(returnedMenu.getName()).isEqualTo("Pasta"));
    }

    @DisplayName("메뉴목록을 조회하는경우 메뉴목록을 반환")
    @Test
    public void returnMenus() {
        List<Menu> menus = getMenus(Menu.builder()
                .id(Arbitraries.longs().between(1, 1000l).sample())
                .menuGroup(MenuGroup.builder().build())
                .build(), 5);
        List<MenuProduct> menuProducts = getMenuProducts(MenuProduct.builder()
                .seq(14l)
                .quantity(15l)
                .productId(14l)
                .menu(Menu.builder().id(13l).build())
                .build(), 5);
        doReturn(menus)
                .when(menuDao)
                .findAll();
        doReturn(menuProducts)
                .when(menuProductDao)
                .findAllByMenu(any(Menu.class));

        List<MenuResponse> returnedMenus = menuService.list();
        assertThat(returnedMenus.stream().map(MenuResponse::getId).collect(Collectors.toList()))
                .containsAll(menus.stream().map(menu -> menu.getId()).collect(Collectors.toList()));
    }

    private List<Menu> getMenus(Menu menu, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(value -> Menu.builder()
                        .id(menu.getId())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .menuProducts(menu.getMenuProducts())
                        .menuGroup(menu.getMenuGroup())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MenuProduct> getMenuProducts(MenuProduct menuProduct, int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(value -> MenuProduct.builder()
                        .seq(menuProduct.getSeq())
                        .productId(menuProduct.getProductId())
                        .menu(menuProduct.getMenu())
                        .quantity(menuProduct.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

}
