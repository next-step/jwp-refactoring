package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

class MenuServiceTest extends IntegrationServiceTest {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;

    private static Product savedProduct;
    private static MenuGroup savedMenuGroup;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        final Product product = ProductServiceTest.makeProduct("후라이드", new BigDecimal(16000));
        savedProduct = productService.create(product);

        // given
        final MenuGroup menuGroup = MenuGroupServiceTest.makeMenuGroup("한마리메뉴");
        savedMenuGroup = menuGroupService.create(menuGroup);
    }

    @Test
    void create() {
        // given
        final Menu menu = makeMenu("후라이드치킨", new BigDecimal(16000), savedMenuGroup.getId(), savedProduct.getId(), 1);

        // when
        final Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo("후라이드치킨");
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(savedMenuGroup.getId());
        assertThat(savedMenu.getMenuProducts()).isNotEmpty();
        assertThat(savedMenu.getMenuProducts().get(0).getSeq()).isNotNull();
        assertThat(savedMenu.getMenuProducts().get(0).getMenuId()).isEqualTo(savedMenu.getId());
        assertThat(savedMenu.getMenuProducts().get(0).getProductId()).isEqualTo(savedProduct.getId());
        assertThat(savedMenu.getMenuProducts().get(0).getQuantity()).isEqualTo(1);
    }

    @DisplayName("가격이 null이거나, 음수이거나, 상품의 가격 * 메뉴 상품의 수량의 총합보다 메뉴의 가격이 클 때 예외 발생")
    @ParameterizedTest
    @MethodSource("provideInvalidPrice")
    void createByInvalidPrice(final BigDecimal price) {
        // given
        final Menu menu = makeMenu("후라이드치킨", price, savedMenuGroup.getId(), savedProduct.getId(), 1);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    private static Stream<BigDecimal> provideInvalidPrice() {
        return Stream.of(null, new BigDecimal(-1), new BigDecimal(16001));
    }

    @Test
    void list() {
        // given
        final Menu menu = makeMenu("후라이드치킨", new BigDecimal(16000), savedMenuGroup.getId(), savedProduct.getId(), 1);
        final Menu savedMenu = menuService.create(menu);

        // when
        final List<Menu> menus = menuService.list();

        // then
        assertThat(menus.get(0).getId()).isNotNull();
        assertThat(menus.get(0).getName()).isEqualTo("후라이드치킨");
        assertThat(menus.get(0).getMenuGroupId()).isEqualTo(savedMenuGroup.getId());
        assertThat(menus.get(0).getMenuProducts()).isNotEmpty();
        assertThat(menus.get(0).getMenuProducts().get(0).getSeq()).isNotNull();
        assertThat(menus.get(0).getMenuProducts().get(0).getMenuId()).isEqualTo(savedMenu.getId());
        assertThat(menus.get(0).getMenuProducts().get(0).getProductId()).isEqualTo(savedProduct.getId());
        assertThat(menus.get(0).getMenuProducts().get(0).getQuantity()).isEqualTo(1);
    }

    public static Menu makeMenu(
        final String name, final BigDecimal price, final Long menuGroupId, final Long productId, final int quantity
    ) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        menu.setMenuProducts(Collections.singletonList(menuProduct));
        return menu;
    }
}
