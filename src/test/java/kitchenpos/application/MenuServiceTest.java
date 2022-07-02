package kitchenpos.application;

import static kitchenpos.utils.generator.MenuFixtureGenerator.generateMenu;
import static kitchenpos.utils.generator.MenuFixtureGenerator.generateMenuProduct;
import static kitchenpos.utils.generator.MenuGroupFixtureGenerator.generateMenuGroup;
import static kitchenpos.utils.generator.ProductFixtureGenerator.generateProductMock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.application.menu.MenuService;
import kitchenpos.dao.menu.MenuDao;
import kitchenpos.dao.menu.MenuGroupDao;
import kitchenpos.dao.menu.MenuProductDao;
import kitchenpos.dao.product.ProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
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
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴를 생성한다.")
    public void createMenu() {
        // Given
        MenuGroup menuGroup = generateMenuGroup();
        Product firstProduct = generateProductMock();
        Product secondProduct = generateProductMock();
        List<Product> products = Arrays.asList(firstProduct, secondProduct);
        Menu givenMenu = generateMenu(menuGroup, firstProduct, secondProduct);
        List<MenuProduct> menuProducts = givenMenu.getMenuProducts();

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(menuDao.save(any(Menu.class))).will(AdditionalAnswers.returnsFirstArg());
        given(productDao.findById(products.get(0).getId())).willReturn(Optional.of(products.get(0)));
        given(productDao.findById(products.get(1).getId())).willReturn(Optional.of(products.get(1)));
        given(menuProductDao.save(menuProducts.get(0))).willReturn(menuProducts.get(0));
        given(menuProductDao.save(menuProducts.get(1))).willReturn(menuProducts.get(1));

        // When
        Menu actualMenu = menuService.create(givenMenu);

        // Then
        verify(menuGroupDao).existsById(any());
        verify(productDao, times(products.size())).findById(anyLong());
        verify(menuDao).save(any(Menu.class));
        verify(menuProductDao, times(menuProducts.size())).save(any(MenuProduct.class));

        assertThat(actualMenu.getMenuProducts())
            .extracting(MenuProduct::getProductId)
            .containsExactly(firstProduct.getId(), secondProduct.getId());
    }

    @ParameterizedTest(name = "case[{index}] : ''{0}'' => {1}")
    @MethodSource
    @DisplayName("메뉴 가격이 유효하지 않은 경우 예외가 발생한다.")
    public void throwException_WhenMenuPriceIsInvalid(final BigDecimal givenPrice, final String givenDescription) {
        // Given
        Menu givenMenu = new Menu();
        givenMenu.setPrice(givenPrice);

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .as(givenDescription)
            .isThrownBy(() -> menuService.create(givenMenu));
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
        final Menu givenMenu = new Menu();
        givenMenu.setPrice(new BigDecimal(1000));

        given(menuGroupDao.existsById(any())).willReturn(false);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(givenMenu));

        verify(menuGroupDao).existsById(any());
    }

    @Test
    @DisplayName("메뉴에 구성되는 메뉴 상품이 존재하지 않는 경우 예외가 발생한다.")
    public void throwException_WhenMenuProductIsNotExist() {
        // Given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(1000));
        List<MenuProduct> menuProducts = generateMenuProduct(generateProductMock());
        menu.setMenuProducts(menuProducts);

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willThrow(IllegalArgumentException.class);

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menu));

        // Then
        verify(menuGroupDao).existsById(any());
        verify(productDao).findById(any());
    }

    @Test
    @DisplayName("메뉴에 포함된 상품의 가격의 총 합이 메뉴의 가격보다 큰 경우 예외가 발생한다.")
    public void throwException_WhenMenuPriceIsOverThanSumOfEachMenuProductsPrice() {
        MenuGroup menuGroup = generateMenuGroup();
        Product firstProduct = generateProductMock();
        Menu givenMenu = generateMenu(menuGroup, firstProduct);

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(firstProduct.getId())).willReturn(Optional.of(firstProduct));

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(givenMenu));

        // Then
        verify(menuGroupDao).existsById(any());
        verify(productDao).findById(anyLong());
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다. : 조회된 메뉴의 수만큼 메뉴 상품을 조회하는 과정에서 `N+1` 문제가 발생한다.")
    public void getAllMenus() {
        // Given
        final int generateMenuMockCount = 5;
        List<Menu> givenMenus = generateMenuMocks(generateMenuMockCount);

        given(menuDao.findAll()).willReturn(givenMenus);
        for (int i = 0; i < generateMenuMockCount; i++) {
            given(menuProductDao.findAllByMenuId(any())).willReturn(givenMenus.get(i).getMenuProducts());
        }

        // When
        List<Menu> actualMenus = menuService.list();

        // Then
        verify(menuDao).findAll();
        verify(menuProductDao, times(generateMenuMockCount)).findAllByMenuId(any());

        List<String> givenMenuNames = givenMenus.stream()
            .map(Menu::getName)
            .collect(Collectors.toList());
        assertThat(actualMenus).extracting(Menu::getName).isEqualTo(givenMenuNames);
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
