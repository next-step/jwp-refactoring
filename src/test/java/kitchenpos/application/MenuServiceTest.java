package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @InjectMocks
    private MenuService menuService;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    private MenuGroup 추천메뉴;
    private Product 타코야끼;
    private Product 뿌링클;

    @BeforeEach
    void setUp() {
        //background
        타코야끼 = new Product(1L, "타코야끼", BigDecimal.valueOf(12000));
        뿌링클 = new Product(2L, "뿌링클", BigDecimal.valueOf(15000));
        추천메뉴 = new MenuGroup(1L, "추천메뉴");

    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        //given
        given(productDao.findById(타코야끼.getId()))
            .willReturn(Optional.ofNullable(타코야끼));
        given(productDao.findById(뿌링클.getId()))
            .willReturn(Optional.ofNullable(뿌링클));
        given(menuGroupDao.existsById(추천메뉴.getId()))
            .willReturn(true);

        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼.getId(), 3L),
            new MenuProduct(뿌링클.getId(), 1L));

        menuProducts.stream()
            .forEach(menuProduct -> given(menuProductDao.save(menuProduct))
                .willReturn(menuProduct));

        Menu menu = new Menu("타코야끼와 뿌링클", BigDecimal.valueOf(51000), 추천메뉴.getId(), menuProducts);
        given(menuDao.save(any()))
            .willReturn(menu);

        //when
        Menu savedMenu = menuService.create(menu);

        //then
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
    }

    @DisplayName("메뉴 가격은 0 이상이어야 한다.")
    @Test
    void create_exception1() {
        //given
        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼.getId(), 3L),
            new MenuProduct(뿌링클.getId(), 1L));

        Menu menu = new Menu("타코야끼와 뿌링클", BigDecimal.valueOf(-1), 추천메뉴.getId(), menuProducts);

        //when, then
        assertThatThrownBy(() -> {
            Menu savedMenu = menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격은 상품 리스트의 합보다 작아야 한다.")
    @Test
    void create_exception2() {
        //given
        given(productDao.findById(타코야끼.getId()))
            .willReturn(Optional.ofNullable(타코야끼));
        given(productDao.findById(뿌링클.getId()))
            .willReturn(Optional.ofNullable(뿌링클));
        given(menuGroupDao.existsById(추천메뉴.getId()))
            .willReturn(true);

        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼.getId(), 3L),
            new MenuProduct(뿌링클.getId(), 1L));

        Menu menu = new Menu("타코야끼와 뿌링클", BigDecimal.valueOf(51001), 추천메뉴.getId(), menuProducts);

        //when, then
        assertThatThrownBy(() -> {
            Menu savedMenu = menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼.getId(), 3L),
            new MenuProduct(뿌링클.getId(), 1L));
        List<Menu> menus = Arrays.asList(new Menu(1L, "타코야끼와 뿌링클", BigDecimal.valueOf(51000),
            추천메뉴.getId(), menuProducts));
        given(menuDao.findAll())
            .willReturn(menus);
        given(menuProductDao.findAllByMenuId(any()))
            .willReturn(menuProducts);

        //when
        List<Menu> findMenus = menuService.list();

        //then
        assertThat(findMenus.size()).isEqualTo(menus.size());
        assertThat(findMenus).containsAll(menus);
    }
}
