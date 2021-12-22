package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductDao;
import kitchenpos.menu.testfixtures.MenuGroupTestFixtures;
import kitchenpos.menu.testfixtures.MenuTestFixtures;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.product.testfixtures.ProductTestFixtures;
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
    private MenuProductDao menuProductDao;

    @Mock
    private MenuGroupDao menuGroupDao;

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
        ProductTestFixtures.상품_조회시_응답_모킹(productDao, 타코야끼);
        ProductTestFixtures.상품_조회시_응답_모킹(productDao, 뿌링클);
        MenuGroupTestFixtures.메뉴_그룹_존재여부_조회시_응답_모킹(menuGroupDao, 추천메뉴, true);

        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼.getId(), 3L),
            new MenuProduct(뿌링클.getId(), 1L));
        MenuTestFixtures.메뉴상품_저장_결과_모킹(menuProductDao, menuProducts);

        Menu menu = new Menu("타코야끼와 뿌링클", BigDecimal.valueOf(51000), 추천메뉴.getId(),
            menuProducts);
        MenuTestFixtures.메뉴_저장_결과_모킹(menuDao, menu);

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

        Menu menu = new Menu("타코야끼와 뿌링클", BigDecimal.valueOf(-1), 추천메뉴.getId(),
            menuProducts);

        //when, then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(
            IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격은 상품 리스트의 합보다 작거나 같아야 한다.")
    @Test
    void create_exception2() {
        //given
        ProductTestFixtures.상품_조회시_응답_모킹(productDao, 타코야끼);
        ProductTestFixtures.상품_조회시_응답_모킹(productDao, 뿌링클);
        MenuGroupTestFixtures.메뉴_그룹_존재여부_조회시_응답_모킹(menuGroupDao, 추천메뉴, true);

        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼.getId(), 3L),
            new MenuProduct(뿌링클.getId(), 1L));

        Menu menu = new Menu("타코야끼와 뿌링클", BigDecimal.valueOf(51001),
            추천메뉴.getId(), menuProducts);

        //when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼.getId(), 3L),
            new MenuProduct(뿌링클.getId(), 1L));
        List<Menu> menus = Arrays.asList(
            new Menu(1L, "타코야끼와 뿌링클", BigDecimal.valueOf(51000), 추천메뉴.getId(),
                menuProducts));
        MenuTestFixtures.메뉴_전체조회_모킹(menuDao, menus);
        MenuTestFixtures.특정_메뉴상품_조회_결과_모킹(menuProductDao, menuProducts);

        //when
        List<Menu> findMenus = menuService.list();

        //then
        assertThat(findMenus.size()).isEqualTo(menus.size());
        assertThat(findMenus).containsAll(menus);
    }
}
