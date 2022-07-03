package kitchenpos.application;

import static kitchenpos.utils.generator.MenuFixtureGenerator.generateCreateMenuRequest;
import static kitchenpos.utils.generator.MenuFixtureGenerator.generateMenu;
import static kitchenpos.utils.generator.MenuFixtureGenerator.generateMenuProduct;
import static kitchenpos.utils.generator.MenuGroupFixtureGenerator.generateMenuGroup;
import static kitchenpos.utils.generator.ProductFixtureGenerator.generateProductMock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.CreateMenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Menu")
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

    private MenuGroup menuGroup;
    private Product firstProduct, secondProduct;
    private Menu menu;

    @BeforeEach
    void setUp() {
        menuGroup = generateMenuGroup();
        firstProduct = generateProductMock();
        secondProduct = generateProductMock();
        menu = generateMenu(menuGroup, firstProduct, secondProduct);
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    public void createMenu() {
        // Given
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        CreateMenuRequest createMenuRequest = generateCreateMenuRequest(menuGroup, firstProduct, secondProduct);

        given(menuGroupRepository.findById(any())).willReturn(Optional.of(menuGroup));
        given(productRepository.findById(firstProduct.getId())).willReturn(Optional.of(firstProduct));
        given(productRepository.findById(secondProduct.getId())).willReturn(Optional.of(secondProduct));
        given(menuRepository.save(any(Menu.class))).will(AdditionalAnswers.returnsFirstArg());
        given(menuProductRepository.saveAll(anyList())).willReturn(menuProducts);

        // When
        menuService.create(createMenuRequest);

        // Then
        verify(menuGroupRepository).findById(any());
        verify(productRepository, times(menuProducts.size())).findById(anyLong());
        verify(menuRepository).save(any(Menu.class));
        verify(menuProductRepository).saveAll(anyList());
    }

    @ParameterizedTest(name = "case[{index}] : ''{0}'' => {1}")
    @MethodSource
    @DisplayName("메뉴 가격이 유효하지 않은 경우 예외가 발생한다.")
    public void throwException_WhenMenuPriceIsInvalid(final BigDecimal givenPrice, final String givenDescription) {
        // Given
        CreateMenuRequest createMenuRequest = new CreateMenuRequest(
            "mock menu name",
            givenPrice,
            null,
            null
        );

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .as(givenDescription)
            .isThrownBy(() -> menuService.create(createMenuRequest));
    }

    private static Stream<Arguments> throwException_WhenMenuPriceIsInvalid() {
        final BigDecimal nullBigDecimal = null;
        return Stream.of(
            Arguments.of(nullBigDecimal, "메뉴가격이 null인 경우 경우"),
            Arguments.of(BigDecimal.valueOf(Integer.MIN_VALUE), "메뉴 가격이 음수인 경우")
        );
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹 정보가 포함된 메뉴를 생성하는 경우 예외가 발생한다.")
    public void throwException_WhenMenuGroupIsNotExist() {
        // Given
        CreateMenuRequest createMenuRequest = new CreateMenuRequest(
            "mock menu name",
            new BigDecimal(1000),
            null,
            null
        );

        given(menuGroupRepository.findById(any())).willThrow(IllegalArgumentException.class);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(createMenuRequest));

        verify(menuGroupRepository).findById(any());
    }

    @Test
    @DisplayName("메뉴에 구성되는 메뉴 상품이 존재하지 않는 경우 예외가 발생한다.")
    public void throwException_WhenMenuProductIsNotExist() {
        // Given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(1000));
        List<MenuProduct> menuProducts = generateMenuProduct(generateProductMock());
        menu.setMenuProducts(menuProducts);

        given(menuGroupRepository.findById(any())).willReturn(Optional.of(menuGroup));
        given(productRepository.findById(any())).willThrow(IllegalArgumentException.class);

        // When
        CreateMenuRequest createMenuRequest = generateCreateMenuRequest(generateMenuGroup(), generateProductMock());
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(createMenuRequest));

        // Then
        verify(menuGroupRepository).findById(any());
        verify(productRepository).findById(any());
    }

    @Test
    @DisplayName("메뉴에 포함된 상품의 가격의 총 합이 메뉴의 가격보다 큰 경우 예외가 발생한다.")
    public void throwException_WhenMenuPriceIsOverThanSumOfEachMenuProductsPrice() {
        // Given
        CreateMenuRequest createMenuRequest = generateCreateMenuRequest(menuGroup, firstProduct);

        given(menuGroupRepository.findById(any())).willReturn(Optional.of(menuGroup));
        given(productRepository.findById(firstProduct.getId())).willReturn(Optional.of(firstProduct));

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(createMenuRequest));

        // Then
        verify(menuGroupRepository).findById(any());
        verify(productRepository).findById(anyLong());
    }

    @Disabled
    @Test
    @DisplayName("메뉴 목록을 조회한다. : 조회된 메뉴의 수만큼 메뉴 상품을 조회하는 과정에서 `N+1` 문제가 발생한다.")
    public void getAllMenus() {
        // Given
        final int generateMenuMockCount = 5;
        List<Menu> givenMenus = generateMenuMocks(generateMenuMockCount);

        given(menuRepository.findAll()).willReturn(givenMenus);

        // When
        List<MenuResponse> actualMenus = menuService.list();

        // Then
        verify(menuRepository).findAll();

        List<String> givenMenuNames = givenMenus.stream()
            .map(Menu::getName)
            .collect(Collectors.toList());
        assertThat(actualMenus).extracting(MenuResponse::getName).isEqualTo(givenMenuNames);
    }

    private List<Menu> generateMenuMocks(int count) {
        List<Menu> menus = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Menu menu = new Menu();
            menu.setMenuProducts(generateMenuProduct(generateProductMock()));
            menus.add(menu);
        }
        return menus;
    }
}
