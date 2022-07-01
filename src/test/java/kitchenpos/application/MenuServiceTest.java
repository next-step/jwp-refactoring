package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.MenuProductTest.메뉴_상품_생성;
import static kitchenpos.domain.MenuTest.메뉴_생성;
import static kitchenpos.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        Product product = 상품_생성("상품", 1_000);
        MenuProduct menuProduct = 메뉴_상품_생성(null, product.getId(), 1);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);
        Menu menu = 메뉴_생성("매뉴", 1_000, 1L, menuProducts);

        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(product));
        when(menuProductDao.save(menuProduct)).thenReturn(menuProduct);
        when(menuDao.save(menu)).thenReturn(메뉴_생성(1L, "매뉴", 1_000, 1L, menuProducts));

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertAll(
                () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualTo(menu.getPrice()),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                () -> assertThat(savedMenu.getMenuProducts()).isEqualTo(menu.getMenuProducts())
        );
    }

    @DisplayName("메뉴의 가격은 0이상이여야 한다")
    @Test
    void createMenu1() {
        // given
        Menu menu = 메뉴_생성("매뉴", -1, null, null);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 메뉴그룹에 포함되어야 한다")
    @Test
    void createMenu2() {
        // given
        Menu menu = 메뉴_생성("매뉴", 1_000, null, null);

        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 정보가 존재하지 않으면 안된다.")
    @Test
    void createMenu3() {
        // given
        Product product = 상품_생성("상품", 1_000);
        MenuProduct menuProduct = 메뉴_상품_생성(null, product.getId(), 1);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);
        Menu menu = 메뉴_생성("매뉴", 2_000, 1L, menuProducts);

        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 상품의 가격의 합보다 크면 안된다.")
    @Test
    void createMenu4() {
        // given
        Product product = 상품_생성("상품", 1_000);
        MenuProduct menuProduct = 메뉴_상품_생성(null, product.getId(), 1);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);
        Menu menu = 메뉴_생성("매뉴", 2_000, 1L, menuProducts);

        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void listMenu() {
        // given
        Menu menu = 메뉴_생성(1L, "매뉴", 1000, null, null);
        List<Menu> menus = Collections.singletonList(menu);
        MenuProduct menuProduct1 = 메뉴_상품_생성(1L, 1L, 1);
        MenuProduct menuProduct2 = 메뉴_상품_생성(1L, 2L, 1);

        when(menuDao.findAll()).thenReturn(menus);
        when(menuProductDao.findAllByMenuId(any())).thenReturn(Arrays.asList(menuProduct1, menuProduct2));

        // when
        List<Menu> list = menuService.list();

        // then
        assertThat(list).containsExactly(menu);
    }
}
