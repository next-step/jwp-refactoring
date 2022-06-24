package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
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
    private MenuProduct menuProduct;
    private Menu menu;
    private Product product;
    private MenuResponse expected;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
        menuProduct = new MenuProduct(1L, 1L, 1L, 2);
        menu = new Menu("후라이드+후라이드", BigDecimal.valueOf(1_000), 1L, Arrays.asList(menuProduct));
        product = new Product(1L, "후라이드", BigDecimal.valueOf(500));
        expected = new MenuResponse(null, "후라이드+후라이드", BigDecimal.valueOf(1_000), 1L,
                Arrays.asList(menuProduct));
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    void createMenu() {
        // given
        final MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 2L);
        final MenuRequest menuRequest = new MenuRequest("후라이드+후라이드", BigDecimal.valueOf(1_000), 1L,
                Arrays.asList(menuProductRequest));
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findAll()).thenReturn(Arrays.asList(product));
        when(menuDao.save(any())).thenReturn(menu);
        when(menuProductDao.save(any())).thenReturn(menuProduct);
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
                        new MenuRequest("후라이드", BigDecimal.valueOf(-1_000), 1L, null)
                )
        );
    }

    @Test
    @DisplayName("생성하려는 메뉴는 현존하는 메뉴 그룹에 없으면 에러 발생")
    void invalidNotContainMenuGroup() {
        // given
        final MenuRequest menuRequest = new MenuRequest("후라이드+후라이드", BigDecimal.valueOf(1_000), 99L, null);
        when(menuGroupDao.existsById(any())).thenReturn(false);
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest));
    }

    @Test
    @DisplayName("메뉴를 구성하는 상품들이 존재하지 않으면 에러 발생")
    void invalidNotExistProduct() {
        // given
        final MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 2L);
        final MenuRequest menuRequest = new MenuRequest("후라이드+후라이드", BigDecimal.valueOf(1_000), 1L,
                Arrays.asList(menuProductRequest));
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findAll()).thenReturn(Collections.emptyList());
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest));
    }

    @Test
    @DisplayName("메뉴 가격은 구성하는 상품들의 합보다 크면 에러 발생")
    void invalidPriceMoreThenProductSum() {
        // given
        final MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 2L);
        final MenuRequest invalidMenuRequest = new MenuRequest("잘못된_메뉴", BigDecimal.valueOf(100_000), 1L,
                Arrays.asList(menuProductRequest));
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findAll()).thenReturn(Arrays.asList(product));
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(invalidMenuRequest));
    }

    @Test
    @DisplayName("메뉴를 조회할 수 있다.")
    void searchMenus() {
        // given
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu));
        when(menuProductDao.findAllByMenuId(any())).thenReturn(Arrays.asList(menuProduct));
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
                () -> assertThat(menuResponse1.getMenuGroupId()).isEqualTo(menuResponse2.getMenuGroupId()),
                () -> assertThat(menuResponse1.getMenuProducts()).hasSize(menuResponse2.getMenuProducts().size())
        );
    }
}
