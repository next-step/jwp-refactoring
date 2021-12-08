package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static kitchenpos.application.MenuGroupServiceTest.menuGroup;
import static kitchenpos.application.ProductServiceTest.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    private MenuDao menuDao;
    private MenuGroupDao menuGroupDao;
    private MenuProductDao menuProductDao;
    private ProductDao productDao;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuDao = mock(MenuDao.class);
        menuGroupDao = mock(MenuGroupDao.class);
        menuProductDao = mock(MenuProductDao.class);
        productDao = mock(ProductDao.class);
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    static Stream<Arguments> baseDB() {
        final Menu menu = menu(1L, "후라이드치킨", BigDecimal.valueOf(15000), 1L);
        final MenuGroup menuGroup = menuGroup(1L, "후라이드 한마리");
        final Product product = product(1L, "후라이드", BigDecimal.valueOf(15000));
        final MenuProduct menuProduct = menuProduct(1L, menu, product, 1L);
        menu.setMenuProducts(new ArrayList<>(Arrays.asList(menuProduct)));

        return Stream.of(arguments(menu, menuGroup, product, menuProduct));
    }

    @ParameterizedTest
    @MethodSource("baseDB")
    void 메뉴_생성(Menu menu, MenuGroup menuGroup, Product product, MenuProduct menuProduct) {
        // given
        when(menuGroupDao.existsById(anyLong())).thenReturn(true);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
        when(menuDao.save(any(Menu.class))).thenReturn(menu);
        when(menuProductDao.save(any(MenuProduct.class))).thenReturn(menuProduct);

        // when
        Menu actual = menuService.create(menu);

        // then
        assertAll(
                () -> assertThat(actual).isEqualTo(menu),
                () -> assertThat(actual.getMenuGroupId()).isEqualTo(menuGroup.getId()),
                () -> assertThat(actual.getMenuProducts()).contains(menuProduct)
        );
    }

    @ParameterizedTest
    @MethodSource("baseDB")
    void 메뉴_생성_메뉴그룹_존재하지_않음(Menu menu, MenuGroup menuGroup, Product product, MenuProduct menuProduct) {
        // given
        when(menuGroupDao.existsById(anyLong())).thenReturn(false);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
        when(menuDao.save(any(Menu.class))).thenReturn(menu);
        when(menuProductDao.save(any(MenuProduct.class))).thenReturn(menuProduct);

        // when
        assertThatThrownBy(() -> {
            Menu actual = menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("baseDB")
    void 메뉴_생성_상품_존재하지_않음(Menu menu, MenuGroup menuGroup, Product product, MenuProduct menuProduct) {
        // given
        when(menuGroupDao.existsById(anyLong())).thenReturn(true);
        when(productDao.findById(anyLong())).thenReturn(Optional.empty());
        when(menuDao.save(any(Menu.class))).thenReturn(menu);
        when(menuProductDao.save(any(MenuProduct.class))).thenReturn(menuProduct);

        // when
        assertThatThrownBy(() -> {
            Menu actual = menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("baseDB")
    void 메뉴_생성_금액_오류(Menu menu, MenuGroup menuGroup, Product product, MenuProduct menuProduct) {
        // given
        product.setPrice(BigDecimal.valueOf(0));
        when(menuGroupDao.existsById(anyLong())).thenReturn(true);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
        when(menuDao.save(any(Menu.class))).thenReturn(menu);
        when(menuProductDao.save(any(MenuProduct.class))).thenReturn(menuProduct);

        // when
        assertThatThrownBy(() -> {
            Menu actual = menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("baseDB")
    void 메뉴_내역_조회() {
        // given
        final Menu menu = menu(1L, "후라이드치킨", BigDecimal.valueOf(15000), 1L);
        final MenuGroup menuGroup = menuGroup(1L, "후라이드 한마리");
        final Product product = product(1L, "후라이드", BigDecimal.valueOf(1000));
        final MenuProduct menuProduct = menuProduct(1L, menu, product, 1L);
        menu.setMenuProducts(new ArrayList<>(Arrays.asList(menuProduct)));
        when(menuDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(menu)));

        List<Menu> actual = menuService.list();

        assertAll(
                () -> assertThat(actual).contains(menu),
                () -> assertThat(actual).hasSize(1)
        );

    }

    public static Menu menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    public static MenuProduct menuProduct(Long seq, Menu menu, Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}