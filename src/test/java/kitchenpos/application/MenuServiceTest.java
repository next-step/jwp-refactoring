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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @Test
    @DisplayName("주어진 메뉴를 저장하고, 저장된 객체를 리턴한다.")
    void create_with_valid_menu() {
        MenuProduct menuProduct1 = new MenuProduct(1L, 1L, 1);
        MenuProduct menuProduct2 = new MenuProduct(2L, 2L, 1);
        Product product1 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        Product product2 = new Product(2L, "양념치킨", BigDecimal.valueOf(16000));
        Menu givenMenu = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(32000), 2L, Arrays.asList(menuProduct1, menuProduct2));
        when(productDao.findById(any()))
                .thenReturn(Optional.of(product1));
        when(menuGroupDao.existsById(any()))
                .thenReturn(true);
        when(productDao.findById(any()))
                .thenReturn(Optional.of(product2));
        when(menuDao.save(any(Menu.class)))
                .thenReturn(givenMenu);

        Menu actual = menuService.create(givenMenu);

        assertThat(actual).isEqualTo(givenMenu);
    }

    @Test
    @DisplayName("가격이 없는 메뉴를 저장시 예외를 던진다.")
    void create_menu_with_no_price() {
        Menu givenMenu = new Menu(1L, "후라이드치킨", null, 2L);

        assertThatThrownBy(() -> menuService.create(givenMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 음수인 메뉴 저장시 예외를 던진다.")
    void create_menu_with_negative_price() {
        Menu givenMenu = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(-1000), 2L);

        assertThatThrownBy(() -> menuService.create(givenMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹에 포함되지 않은 메뉴 저장시 예외를 던진다")
    void create_menu_with_no_menu_group_id() {
        Menu givenMenu = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(1000), null);

        assertThatThrownBy(() -> menuService.create(givenMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품이 포함되지 않은 메뉴 저장시 예외를 던진다")
    void create_menu_with_no_products() {
        MenuProduct menuProduct1 = new MenuProduct(1L, 1L, 1);
        MenuProduct menuProduct2 = new MenuProduct(2L, 2L, 1);
        Menu givenMenu = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), 2L, Arrays.asList(menuProduct1, menuProduct2));
        when(menuGroupDao.existsById(any()))
                .thenReturn(true);

        assertThatThrownBy(() -> menuService.create(givenMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴에 포함된 상품 합이 올바르지 않게 주어지면 예외를 던진다")
    void create_with_menu_and_invalid_price() {
        MenuProduct menuProduct1 = new MenuProduct(1L, 1L, 1);
        MenuProduct menuProduct2 = new MenuProduct(2L, 2L, 1);
        Product product1 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        Product product2 = new Product(2L, "양념치킨", BigDecimal.valueOf(16000));
        Menu givenMenu = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(33000), 2L, Arrays.asList(menuProduct1, menuProduct2));
        when(menuGroupDao.existsById(any()))
                .thenReturn(true);
        when(productDao.findById(any()))
                .thenReturn(Optional.of(product1));
        when(productDao.findById(any()))
                .thenReturn(Optional.of(product2));

        assertThatThrownBy(() -> menuService.create(givenMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴를 조회한다")
    @Test
    void list() {
        final Menu givenMenu1 = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), 2L);
        final Menu givenMenu2 = new Menu(2L, "양념치킨", BigDecimal.valueOf(16000), 2L);

        when(menuDao.findAll())
                .thenReturn(Arrays.asList(givenMenu1, givenMenu2));
        List<Menu> menus = menuService.list();

        assertThat(menus).containsExactly(givenMenu1, givenMenu2);
    }
}
