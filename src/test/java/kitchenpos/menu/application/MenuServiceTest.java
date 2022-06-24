package kitchenpos.menu.application;

import static kitchenpos.utils.DomainFixtureFactory.createMenu;
import static kitchenpos.utils.DomainFixtureFactory.createMenuProduct;
import static kitchenpos.utils.DomainFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
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
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private MenuService menuService;

    private Menu 양념치킨;
    private MenuProduct 양념치킨상품;
    private Product 양념;

    @BeforeEach
    void setUp() {
        양념 = createProduct(1L, "양념", BigDecimal.valueOf(20000L));
        양념치킨상품 = createMenuProduct(1L, 1L, 1L, 2L);
        양념치킨 = createMenu(1L, "양념치킨", BigDecimal.valueOf(40000L), 1L, Lists.newArrayList(양념치킨상품));
    }

    @DisplayName("메뉴 생성 테스트")
    @Test
    void create() {
        given(menuGroupDao.existsById(양념치킨.getMenuGroupId())).willReturn(true);
        given(productDao.findById(양념치킨상품.getProductId())).willReturn(Optional.ofNullable(양념));
        given(menuDao.save(양념치킨)).willReturn(양념치킨);
        given(menuProductDao.save(양념치킨상품)).willReturn(양념치킨상품);
        Menu menu = menuService.create(양념치킨);
        assertAll(
                () -> assertThat(menu.getName()).isEqualTo("양념치킨"),
                () -> assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(40000L)),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(1L),
                () -> assertThat(menu.getMenuProducts()).containsExactlyElementsOf(Lists.newArrayList(양념치킨상품))
        );
    }

    @DisplayName("메뉴 생성시 가격이 없는 경우 테스트")
    @Test
    void createWithPriceNull() {
        Menu menu = createMenu(1L, "양념치킨", null, 1L, Lists.newArrayList(양념치킨상품));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 생성시 가격이 0원 아래인 경우 테스트")
    @Test
    void createWithPriceUnderZero() {
        Menu menu = createMenu(1L, "양념치킨", BigDecimal.valueOf(-100L), 1L, Lists.newArrayList(양념치킨상품));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 생성시 메뉴그룹이 등록이 안되어있는 경우 테스트")
    @Test
    void createWithNotFoundMenuGroup() {
        given(menuGroupDao.existsById(양념치킨.getMenuGroupId())).willReturn(false);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(양념치킨));
    }

    @DisplayName("메뉴 생성시 상품이 등록이 안되어있는 경우 테스트")
    @Test
    void createWithNotFoundProduct() {
        given(menuGroupDao.existsById(양념치킨.getMenuGroupId())).willReturn(true);
        given(productDao.findById(양념치킨상품.getProductId())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(양념치킨));
    }

    @DisplayName("메뉴가 갖는 상품들 각각의 가격과 수량을 곱해서 더한 총 금액이 메뉴 가격보다 낮은 경우 테스트")
    @Test
    void createWithTotalPriceLessThanMenuPrice() {
        양념치킨 = createMenu(1L, "양념치킨", BigDecimal.valueOf(50000L), 1L, Lists.newArrayList(양념치킨상품));
        given(menuGroupDao.existsById(양념치킨.getMenuGroupId())).willReturn(true);
        given(productDao.findById(양념치킨상품.getProductId())).willReturn(Optional.ofNullable(양념));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(양념치킨));
    }

    @DisplayName("메뉴 목록 조회 테스트")
    @Test
    void list() {
        given(menuDao.findAll()).willReturn(Lists.newArrayList(양념치킨));
        given(menuProductDao.findAllByMenuId(양념치킨.getId())).willReturn(Lists.newArrayList(양념치킨상품));
        List<Menu> menus = menuService.list();
        assertThat(menus).containsExactlyElementsOf(Lists.newArrayList(양념치킨));
    }
}
