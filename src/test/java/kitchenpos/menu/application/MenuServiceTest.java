package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.dao.MenuDao;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.testfixtures.MenuGroupTestFixtures;
import kitchenpos.menu.testfixtures.MenuTestFixtures;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
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
    private MenuGroupService menuGroupService;

    @Mock
    private ProductService productService;

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
        ProductTestFixtures.상품_조회시_응답_모킹(productService, 타코야끼);
        ProductTestFixtures.상품_조회시_응답_모킹(productService, 뿌링클);
        MenuGroupTestFixtures.메뉴_그룹_존재여부_조회시_응답_모킹(menuGroupService, 추천메뉴, true);

        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼, 3L),
            new MenuProduct(뿌링클, 1L));

        Menu menu = new Menu("타코야끼와 뿌링클", BigDecimal.valueOf(51000), 추천메뉴,
            menuProducts);
        MenuTestFixtures.메뉴_저장_결과_모킹(menuDao, menu);

        //when
        MenuRequest menuRequest = MenuTestFixtures.convertToMenuRequest(menu);
        MenuResponse menuResponse = menuService.create(menuRequest);

        //then
        assertThat(menuResponse.getName()).isEqualTo(menu.getName());
    }

    @DisplayName("메뉴 가격은 0 이상이어야 한다.")
    @Test
    void create_exception1() {
        //given
        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼, 3L),
            new MenuProduct(뿌링클, 1L));

        //when, then
        assertThatThrownBy(() -> new Menu("타코야끼와 뿌링클", BigDecimal.valueOf(-1), 추천메뉴,
            menuProducts));
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(타코야끼, 3L),
            new MenuProduct(뿌링클, 1L));
        List<Menu> menus = Arrays.asList(
            new Menu(1L, "타코야끼와 뿌링클", BigDecimal.valueOf(51000), 추천메뉴,
                menuProducts));
        MenuTestFixtures.메뉴_전체조회_모킹(menuDao, menus);

        //when
        List<MenuResponse> findMenus = menuService.list();

        //then
        assertThat(findMenus.size()).isEqualTo(menus.size());
        메뉴목록_검증(findMenus, menus);
    }

    private void 메뉴목록_검증(List<MenuResponse> findMenus, List<Menu> menus) {
        List<Long> findProductIds = findMenus.stream()
            .map(MenuResponse::getId)
            .collect(Collectors.toList());
        List<Long> expectProductIds = menus.stream()
            .map(Menu::getId)
            .collect(Collectors.toList());
        assertThat(findProductIds).containsAll(expectProductIds);
    }
}
