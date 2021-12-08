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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.MenuGroupServiceTest.menuGroup;
import static kitchenpos.application.ProductServiceTest.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    private MenuDao menuDao;
    private MenuGroupDao menuGroupDao;
    private MenuProductDao menuProductDao;
    private ProductDao productDao;
    private MenuService menuService;

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

    @BeforeEach
    void setUp() {
        menuDao = mock(MenuDao.class);
        menuGroupDao = mock(MenuGroupDao.class);
        menuProductDao = mock(MenuProductDao.class);
        productDao = mock(ProductDao.class);
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    Menu setMockData(boolean groupStatus) {
        final Menu menu = menu(1L, "후라이드치킨", BigDecimal.valueOf(15000), 1L);
        final MenuGroup menuGroup = menuGroup(1L, "후라이드 한마리");
        final Product product = product(1L, "후라이드", BigDecimal.valueOf(15000));
        final MenuProduct menuProduct = menuProduct(1L, menu, product, 1L);
        menu.setMenuProducts(new ArrayList<>(Arrays.asList(menuProduct)));

        when(menuGroupDao.existsById(anyLong())).thenReturn(groupStatus);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
        when(menuDao.save(any(Menu.class))).thenReturn(menu);
        when(menuProductDao.save(any(MenuProduct.class))).thenReturn(menuProduct);

        return menu;
    }

    @Test
    void 메뉴_생성() {
        // given
        boolean isExistsMenuGroup = true;
        Menu menu = setMockData(isExistsMenuGroup);

        // when
        Menu actual = menuService.create(menu);

        // then
        assertAll(
                () -> assertThat(actual).isEqualTo(menu),
                () -> assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                () -> assertThat(actual.getMenuProducts()).containsAll(menu.getMenuProducts())
        );
    }

    @Test
    void 메뉴_생성_메뉴그룹_존재하지_않음() {
        // given
        boolean isExistsMenuGroup = false;
        Menu menu = setMockData(isExistsMenuGroup);

        // when
        assertThatThrownBy(() -> {
            Menu actual = menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성_상품_존재하지_않음() {
        // given
        boolean isExistsMenuGroup = true;
        Menu menu = setMockData(isExistsMenuGroup);
        when(productDao.findById(anyLong())).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> {
            Menu actual = menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성_상품_금액_오류() {
        // given
        boolean isExistsMenuGroup = true;
        Menu menu = setMockData(isExistsMenuGroup);
        final Product product = product(1L, "후라이드", BigDecimal.valueOf(1000));
        when(productDao.findById(anyLong())).thenReturn(Optional.of(product));

        // when
        assertThatThrownBy(() -> {
            Menu actual = menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
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

}
