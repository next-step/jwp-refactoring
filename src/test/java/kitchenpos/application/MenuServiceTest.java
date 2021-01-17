package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("메뉴 서비스")
public class MenuServiceTest extends ServiceTestBase {
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuService menuService;

    private MenuGroupResponse menuGroup;
    private ProductResponse product;

    @Autowired
    public MenuServiceTest(MenuGroupService menuGroupService, ProductService productService, MenuService menuService) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuService = menuService;
    }

    @BeforeEach
    void setUp() {
        super.setUp();

        menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void createMenu() {
        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(product.getId(), 2L));
        Menu menu = createMenu("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts);
        Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu.getId()).isNotNull();
    }

    @DisplayName("가격이 부적합한 메뉴를 등록한다")
    @ParameterizedTest
    @MethodSource
    void createMenuWithIllegalArguments(Long price) {
        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(product.getId(), 2L));
        Menu menu = createMenu("후라이드+후라이드", price, menuGroup.getId(), menuProducts);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    private static Stream<Arguments> createMenuWithIllegalArguments() {
        return Stream.of(
                Arguments.of((Object)null),
                Arguments.of(-1L),
                Arguments.of(300000L)
        );
    }

    @DisplayName("메뉴 그룹이 등록되지 않은 메뉴를 등록한다")
    @Test
    void createMenuWithNotExistsMenuGroup() {
        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(product.getId(), 2L));
        Menu menu = createMenu("후라이드+후라이드", 19_000L, 99L, menuProducts);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("제품이 등록되지 않은 메뉴를 등록한다")
    @Test
    void createMenuWithNotExistsProduct() {
        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(99L, 2L));
        Menu menu = createMenu("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("제품을 조회한다")
    @Test
    void findAllProduct() {
        List<MenuProduct> menuProducts = Collections.singletonList(createMenuProduct(product.getId(), 2L));
        menuService.create(createMenu("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        menuService.create(createMenu("양념+후라이드", 20_000L, menuGroup.getId(), menuProducts));

        List<Menu> menus = menuService.list();
        assertThat(menus.size()).isEqualTo(2);
        List<String> menuNames = menus.stream()
                .map(Menu::getName)
                .collect(Collectors.toList());
        assertThat(menuNames).contains("후라이드+후라이드", "양념+후라이드");
    }

    public static Menu createMenu(String name, Long price, Long menuGroupId, List<MenuProduct> menuProuducts) {
        Menu menu = new Menu();

        menu.setName(name);
        if (price != null) {
            menu.setPrice(new BigDecimal(price));
        }
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProuducts);

        return menu;
    }

    public static MenuProduct createMenuProduct(Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();

        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }
}
