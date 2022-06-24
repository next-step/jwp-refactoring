package kitchenpos.menu.application;

import static kitchenpos.utils.DomainFixtureFactory.createMenu;
import static kitchenpos.utils.DomainFixtureFactory.createMenuGroup;
import static kitchenpos.utils.DomainFixtureFactory.createMenuProduct;
import static kitchenpos.utils.DomainFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuProductRepository menuProductRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuService menuService;

    private Menu 양념치킨;
    private MenuProduct 양념치킨상품;
    private MenuGroup 한마리메뉴;
    private Product 양념;

    @BeforeEach
    void setUp() {
        양념 = createProduct(1L, "양념", BigDecimal.valueOf(20000L));
        양념치킨 = createMenu(1L, "양념치킨", BigDecimal.valueOf(40000L), 한마리메뉴, Lists.newArrayList(양념치킨상품));
        양념치킨상품 = createMenuProduct(1L, 양념치킨, 양념, 2L);
        한마리메뉴 = createMenuGroup(1L, "한마리메뉴");
    }

    @DisplayName("메뉴 생성 테스트")
    @Test
    void create() {
        given(menuGroupRepository.existsById(양념치킨.menuGroup().id())).willReturn(true);
        given(productRepository.findById(양념치킨상품.product().id())).willReturn(Optional.ofNullable(양념));
        given(menuRepository.save(양념치킨)).willReturn(양념치킨);
        given(menuProductRepository.save(양념치킨상품)).willReturn(양념치킨상품);
        Menu menu = menuService.create(양념치킨);
        assertAll(
                () -> assertThat(menu.name()).isEqualTo(Name.of("양념치킨")),
                () -> assertThat(menu.price()).isEqualTo(Price.of(BigDecimal.valueOf(40000L))),
                () -> assertThat(menu.menuGroup()).isEqualTo(한마리메뉴),
                () -> assertThat(menu.menuProducts().readOnlyMenuProducts()).isEqualTo(Lists.newArrayList(양념치킨상품))
        );
    }

    @DisplayName("메뉴 생성시 가격이 없는 경우 테스트")
    @Test
    void createWithPriceNull() {
        Menu menu = createMenu(1L, "양념치킨", null, 한마리메뉴, Lists.newArrayList(양념치킨상품));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu))
                .withMessage("금액을 지정해야 합니다.");
    }

    @DisplayName("메뉴 생성시 가격이 0원 아래인 경우 테스트")
    @Test
    void createWithPriceUnderZero() {
        Menu menu = createMenu(1L, "양념치킨", BigDecimal.valueOf(-100L), 한마리메뉴, Lists.newArrayList(양념치킨상품));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 생성시 메뉴그룹이 등록이 안되어있는 경우 테스트")
    @Test
    void createWithNotFoundMenuGroup() {
        given(menuGroupRepository.existsById(양념치킨.menuGroup().id())).willReturn(false);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(양념치킨));
    }

    @DisplayName("메뉴 생성시 상품이 등록이 안되어있는 경우 테스트")
    @Test
    void createWithNotFoundProduct() {
        given(menuGroupRepository.existsById(양념치킨.menuGroup().id())).willReturn(true);
        given(productRepository.findById(양념치킨상품.product().id())).willReturn(Optional.empty());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(양념치킨));
    }

    @DisplayName("메뉴가 갖는 상품들 각각의 가격과 수량을 곱해서 더한 총 금액이 메뉴 가격보다 낮은 경우 테스트")
    @Test
    void createWithTotalPriceLessThanMenuPrice() {
        양념치킨 = createMenu(1L, "양념치킨", BigDecimal.valueOf(50000L), 한마리메뉴, Lists.newArrayList(양념치킨상품));
        given(menuGroupRepository.existsById(양념치킨.menuGroup().id())).willReturn(true);
        given(productRepository.findById(양념치킨상품.product().id())).willReturn(Optional.ofNullable(양념));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(양념치킨));
    }

    @DisplayName("메뉴 목록 조회 테스트")
    @Test
    void list() {
        given(menuRepository.findAll()).willReturn(Lists.newArrayList(양념치킨));
        given(menuProductRepository.findAllByMenuId(양념치킨.id())).willReturn(Lists.newArrayList(양념치킨상품));
        List<Menu> menus = menuService.list();
        assertThat(menus).containsExactlyElementsOf(Lists.newArrayList(양념치킨));
    }
}
