package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
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

    private Menu menu;

    private Product 후라이드치킨;

    @BeforeEach
    void setUp() {
        후라이드치킨 = new Product(1L, "후라이드치킨", new BigDecimal(16_000));
        MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 1);
        menu = new Menu(1L, "후라이드치킨", new BigDecimal(16_000), 1L, new ArrayList<MenuProduct>(Collections.singleton(menuProduct)));
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void createTest() {
        // given
        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(java.util.Optional.of(후라이드치킨));
        when(menuDao.save(menu)).thenReturn(menu);

        // when
        MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
        Menu returnedMenu = menuService.create(this.menu);

        // then
        assertThat(returnedMenu).isEqualTo(menu);
    }

    @DisplayName("메뉴의 가격이 0원 이상이어야 한다")
    @Test
    void minusMenuPriceTest() {
        // when
        this.menu.setPrice(new BigDecimal(-1000));
        MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        // then
        assertThatThrownBy(() -> menuService.create(this.menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 존재해야 한다")
    @Test
    void nullPriceTest() {
        // when
        this.menu.setPrice(null);
        MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        // then
        assertThatThrownBy(() -> menuService.create(this.menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품의 총합보다 작아야 한다")
    @Test
    void menuPriceLimitTest() {
        // when
        this.menu.setPrice(new BigDecimal(19_000));
        MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        // then
        assertThatThrownBy(() -> menuService.create(this.menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 메뉴 그룹에 속해있어야 한다")
    @Test
    void inMenuGroupTest() {
        // when
        this.menu.setMenuGroupId(null);
        MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        // then
        assertThatThrownBy(() -> menuService.create(this.menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 조회한다")
    @Test
    void list() {
        // given
        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(java.util.Optional.of(후라이드치킨));
        when(menuDao.save(menu)).thenReturn(menu);
        when(menuDao.findAll()).thenReturn(Collections.singletonList(menu));

        // when
        MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
        Menu returnedMenu = menuService.create(this.menu);
        List<Menu> list = menuService.list();

        // then
        assertThat(list).contains(returnedMenu);
    }
}
