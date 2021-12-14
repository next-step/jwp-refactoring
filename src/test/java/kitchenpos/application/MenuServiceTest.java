package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void createMenu() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "한마리메뉴");
        Product product = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1);
        Menu menu = new Menu("후라이드 치킨", BigDecimal.valueOf(16000), menuGroup.getId(),
            Collections.singletonList(menuProduct));

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(product));
        given(menuDao.save(any())).willReturn(menu);
        given(menuProductDao.save(any())).willReturn(menuProduct);

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertEquals(menu, savedMenu);
    }

    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void createMenuWrongPrice() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "한마리메뉴");
        Product product = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1);
        Menu menu = new Menu("후라이드 치킨", BigDecimal.valueOf(-1), menuGroup.getId(),
            Collections.singletonList(menuProduct));

        // when & then
        assertThrows(IllegalArgumentException.class,() -> menuService.create(menu));
    }

    @DisplayName("메뉴그룹이 존재하지 않으면 메뉴를 등록할 수 없다.")
    @Test
    void createMenuNonExistMenuGroup() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "한마리메뉴");
        Product product = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1);
        Menu menu = new Menu("후라이드 치킨", BigDecimal.valueOf(16000), menuGroup.getId(),
            Collections.singletonList(menuProduct));

        given(menuGroupDao.existsById(any())).willReturn(false);

        // when && then
        assertThrows(IllegalArgumentException.class,() -> menuService.create(menu));
    }

    @DisplayName("메뉴의 가격이 메뉴상품 금액의 합보다 크면 등록할 수 없다.")
    @Test
    void createMenuPriceGreaterThanSum() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "한마리메뉴");
        Product product = new Product(1L, "후라이드", BigDecimal.valueOf(0));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1);
        Menu menu = new Menu("후라이드 치킨", BigDecimal.valueOf(16000), menuGroup.getId(),
            Collections.singletonList(menuProduct));

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(product));

        // when & then
        assertThrows(IllegalArgumentException.class,() -> menuService.create(menu));
    }

    @DisplayName("상품이 존재하지 않으면 메뉴를 등록할 수 없다.")
    @Test
    void createMenuNonExistProduct() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "한마리메뉴");
        Product product = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1);
        Menu menu = new Menu("후라이드 치킨", BigDecimal.valueOf(16000), menuGroup.getId(),
            Collections.singletonList(menuProduct));

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class,() -> menuService.create(menu));
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void getMenus() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "한마리메뉴");
        Product product = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1);
        Menu menu = new Menu("후라이드 치킨", BigDecimal.valueOf(16000), menuGroup.getId(),
            Collections.singletonList(menuProduct));
        List<Menu> menus = Collections.singletonList(menu);

        given(menuDao.findAll()).willReturn(menus);
        given(menuProductDao.findAllByMenuId(any()))
            .willReturn(Collections.singletonList(menuProduct));

        // when
        List<Menu> findMenus = menuService.list();

        // then
        assertThat(findMenus)
            .containsExactlyElementsOf(menus);
    }
}
