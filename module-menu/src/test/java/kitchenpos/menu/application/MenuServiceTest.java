package kitchenpos.menu.application;

import static kitchenpos.utils.DomainFixtureFactory.createMenu;
import static kitchenpos.utils.DomainFixtureFactory.createMenuGroup;
import static kitchenpos.utils.DomainFixtureFactory.createMenuProduct;
import static kitchenpos.utils.DomainFixtureFactory.createMenuRequest;
import static kitchenpos.utils.DomainFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.validator.MenuValidator;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
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
    private MenuValidator menuValidator;
    @InjectMocks
    private MenuService menuService;

    private Menu 양념치킨;
    private MenuProduct 양념치킨상품;
    private MenuGroup 한마리메뉴;
    private Product 양념;

    @BeforeEach
    void setUp() {
        양념 = createProduct(1L, "양념", BigDecimal.valueOf(20000L));
        한마리메뉴 = createMenuGroup(1L, "한마리메뉴");
        양념치킨상품 = createMenuProduct(양념.id(), 2L);
        양념치킨 = createMenu("양념치킨", BigDecimal.valueOf(40000L), 한마리메뉴.id(),
                MenuProducts.from(Lists.newArrayList(양념치킨상품)));
    }

    @DisplayName("메뉴 생성 테스트")
    @Test
    void create() {
        MenuRequest menuRequest = createMenuRequest("양념치킨", BigDecimal.valueOf(40000L), 한마리메뉴.id(),
                Lists.newArrayList(new MenuProductRequest(양념.id(), 2L)));
        given(menuRepository.save(any(Menu.class))).willReturn(양념치킨);
        MenuResponse menuResponse = menuService.create(menuRequest);
        assertAll(
                () -> assertThat(menuResponse.getName()).isEqualTo("양념치킨"),
                () -> assertThat(menuResponse.getPrice()).isEqualTo(BigDecimal.valueOf(40000L)),
                () -> assertThat(menuResponse.getMenuGroupId()).isEqualTo(한마리메뉴.id()),
                () -> assertThat(menuResponse.getMenuProducts()).isEqualTo(
                        Lists.newArrayList(MenuProductResponse.from(양념치킨상품)))
        );
    }

    @DisplayName("메뉴 생성시 가격이 없는 경우 테스트")
    @Test
    void createWithPriceNull() {
        MenuRequest menuRequest = createMenuRequest("양념치킨", null, 한마리메뉴.id(),
                Lists.newArrayList(new MenuProductRequest(양념.id(), 2L)));
        willThrow(new IllegalArgumentException("금액을 지정해야 합니다.")).given(menuValidator).validate(menuRequest);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest))
                .withMessage("금액을 지정해야 합니다.");
    }

    @DisplayName("메뉴 생성시 가격이 0원 아래인 경우 테스트")
    @Test
    void createWithPriceUnderZero() {
        MenuRequest menuRequest = createMenuRequest("양념치킨", BigDecimal.valueOf(-40000L), 한마리메뉴.id(),
                Lists.newArrayList(new MenuProductRequest(양념.id(), 2L)));
        willThrow(new IllegalArgumentException("금액은 " + Price.MIN + "원 미만이 될 수 없습니다.")).given(menuValidator)
                .validate(menuRequest);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest))
                .withMessage("금액은 " + Price.MIN + "원 미만이 될 수 없습니다.");
    }

    @DisplayName("메뉴가 갖는 상품들 각각의 가격과 수량을 곱해서 더한 총 금액이 메뉴 가격보다 낮은 경우 테스트")
    @Test
    void createWithTotalPriceLessThanMenuPrice() {
        MenuRequest menuRequest = createMenuRequest("양념치킨", BigDecimal.valueOf(60000L), 한마리메뉴.id(),
                Lists.newArrayList(new MenuProductRequest(양념.id(), 2L)));
        willThrow(new IllegalArgumentException("메뉴 가격은 상품의 총 금액을 넘길 수 없습니다.")).given(menuValidator)
                .validate(menuRequest);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest))
                .withMessage("메뉴 가격은 상품의 총 금액을 넘길 수 없습니다.");
    }

    @DisplayName("메뉴 목록 조회 테스트")
    @Test
    void list() {
        given(menuRepository.findAllWithFetchJoin()).willReturn(Lists.newArrayList(양념치킨));
        List<MenuResponse> menus = menuService.list();
        Assertions.assertThat(menus).containsExactlyElementsOf(Lists.newArrayList(양념치킨).stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList()));
    }
}
