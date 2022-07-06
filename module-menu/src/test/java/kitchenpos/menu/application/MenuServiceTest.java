package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.InvalidPriceException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    private MenuService menuService;
    private MenuGroup menuGroup;
    private MenuProduct menuProduct;
    private MenuProducts menuProducts;
    private Menu menu;
    private Product product;
    private MenuResponse expected;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuValidator menuValidator;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, menuValidator);
        product = new Product(1L, "후라이드", Price.of(500L));
        menuGroup = new MenuGroup(1L, "후라이드세트");
        menuProducts = new MenuProducts(Arrays.asList(menuProduct));
        menu = new Menu.Builder("후라이드+후라이드")
                .setId(1L)
                .setPrice(Price.of(1_000L))
                .setMenuGroup(menuGroup)
                .setMenuProducts(menuProducts)
                .build();
        menuProduct = new MenuProduct.Builder(menu)
                .setSeq(1L)
                .setProductId(product.getId())
                .setQuantity(Quantity.of(2L))
                .build();
        expected = new MenuResponse(null, "후라이드+후라이드", Price.of(1_000L), menuGroup.toMenuGroupResponse(),
                menuProducts.get());
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    void createMenu() {
        // given
        final MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 2L);
        final MenuRequest menuRequest = new MenuRequest("후라이드+후라이드", 1_000L, 1L,
                Arrays.asList(menuProductRequest));
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(menuGroup));
        when(menuValidator.findProductId(any())).thenReturn(product.getId());
        when(menuRepository.save(any())).thenReturn(menu);
        // when
        final MenuResponse actual = menuService.create(menuRequest);
        // then
        verifyMenuResponse(actual, expected);
    }

    @ParameterizedTest(name = "메뉴의 가격은 음수이거나 없으면 예외 발생")
    @MethodSource("nullAndNegativePriceMenuParameter")
    void invalidPriceMenu(MenuRequest menuRequest) {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest));
    }

    public static Stream<Arguments> nullAndNegativePriceMenuParameter() {
        return Stream.of(
                Arguments.of(
                        new MenuRequest("후라이드", null, 1L, null)
                ),
                Arguments.of(
                        new MenuRequest("후라이드", -1_000L, 1L, null)
                )
        );
    }

    @Test
    @DisplayName("생성하려는 메뉴는 현존하는 메뉴 그룹에 없으면 에러 발생")
    void invalidNotContainMenuGroup() {
        // given
        final MenuRequest menuRequest = new MenuRequest("후라이드+후라이드", 1_000L, 99L, null);
        when(menuGroupRepository.findById(any())).thenReturn(Optional.empty());
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest));
    }

    @Test
    @DisplayName("메뉴 가격은 구성하는 상품들의 합보다 크면 에러 발생")
    void invalidPriceMoreThenProductSum() {
        // given
        final MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 2L);
        final MenuRequest invalidMenuRequest = new MenuRequest("잘못된_메뉴", 100_000L, 1L,
                Arrays.asList(menuProductRequest));
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(menuGroup));
        when(menuValidator.findProductId(any())).thenReturn(product.getId());
        doThrow(new InvalidPriceException()).when(menuValidator).validateProductsTotalPrice(any());
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(invalidMenuRequest));
    }

    @Test
    @DisplayName("메뉴를 조회할 수 있다.")
    void searchMenus() {
        // given
        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu));
        // when
        final List<MenuResponse> actual = menuService.list();
        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> verifyMenuResponse(actual.get(0), expected)
        );
    }

    private void verifyMenuResponse(MenuResponse menuResponse1, MenuResponse menuResponse2) {
        assertAll(
                () -> assertThat(menuResponse1.getName()).isEqualTo(menuResponse2.getName()),
                () -> assertThat(menuResponse1.getPrice()).isEqualTo(menuResponse2.getPrice()),
                () -> assertThat(menuResponse1.getMenuGroup().getId()).isEqualTo(menuResponse2.getMenuGroup().getId()),
                () -> assertThat(menuResponse1.getMenuProducts()).hasSize(menuResponse2.getMenuProducts().size())
        );
    }
}
