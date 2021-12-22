package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

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

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        // given
        MenuProduct menuProduct = createMenuProduct(null, 1L, 2, null);
        Mockito.when(menuGroupDao.existsById(Mockito.anyLong()))
            .thenReturn(true);

        Product product = ProductServiceTestHelper.makeProduct(1L, "name", BigDecimal.TEN);
        Mockito.when(productDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(product));

        MenuProduct expectedMenuProduct = createMenuProduct(null, 1L, 2, null);
        Mockito.when(menuProductDao.save(Mockito.any()))
            .thenReturn(expectedMenuProduct);

        Menu expectedMenu = createMenu(1L, "name", BigDecimal.ONE, 1L, Arrays.asList(expectedMenuProduct));
        Mockito.when(menuDao.save(Mockito.any()))
            .thenReturn(expectedMenu);

        Menu menu = createMenu(null, "name", BigDecimal.ONE, 1L, Arrays.asList(menuProduct));

        // when
        Menu actual = menuService.create(menu);

        // then
        assertThat(actual).isEqualTo(expectedMenu);
    }

    @DisplayName("메뉴 상품 가격의 합과 입력받은 가격 비교하여 입력받은 가격이 더 크면 에러")
    @Test
    void createErrorWhenPriceIsBiggerThanSum() {
        // given
        MenuProduct menuProduct = createMenuProduct(null, 1L, 2, null);
        Mockito.when(menuGroupDao.existsById(Mockito.anyLong()))
            .thenReturn(true);

        Product product = ProductServiceTestHelper.makeProduct(1L, "name", BigDecimal.TEN);
        Mockito.when(productDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(product));

        Menu menu = createMenu(null, "name", BigDecimal.valueOf(21), 1L, Arrays.asList(menuProduct));

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 상품 가격의 합과 입력받은 가격 비교하여 입력받은 가격이 더 크면 에러")
    @Test
    void createErrorWhenProductNotExists() {
        // given
        MenuProduct menuProduct = createMenuProduct(null, 1L, 2, null);
        Mockito.when(menuGroupDao.existsById(Mockito.anyLong()))
            .thenReturn(true);

        Mockito.when(productDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.empty());

        Menu menu = createMenu(null, "name", BigDecimal.ONE, 1L, Arrays.asList(menuProduct));

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 상품 가격이 null이면 에러")
    @Test
    void createErrorWhenPriceIsNull() {
        MenuProduct menuProduct = createMenuProduct(null, 1L, 2, null);
        Menu menu = createMenu(null, "name", null, 1L, Arrays.asList(menuProduct));

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 상품 가격이 0 미만이면 에러")
    @Test
    void createErrorWhenPriceIsLessThanZero() {
        MenuProduct menuProduct = createMenuProduct(null, 1L, 2, null);
        Menu menu = createMenu(null, "name", BigDecimal.valueOf(-1), 1L, Arrays.asList(menuProduct));

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 에러 발생")
    @Test
    void createErrorWhenMenuGroupNotExists() {
        // given
        MenuProduct menuProduct = createMenuProduct(null, 1L, 2, null);
        Mockito.when(menuGroupDao.existsById(Mockito.anyLong()))
            .thenReturn(false);

        Menu menu = createMenu(null, "name", BigDecimal.ONE, 1L, Arrays.asList(menuProduct));

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @Test
    void list() {
        // given
        MenuProduct menuProduct = createMenuProduct(null, 1L, 2, null);

        Menu menu1 = createMenu(1L, "name1", BigDecimal.ONE, 1L, Arrays.asList(menuProduct));
        Menu menu2 = createMenu(2L, "name2", BigDecimal.TEN, 1L, Arrays.asList(menuProduct));
        List<Menu> menus = Arrays.asList(menu1, menu2);
        Mockito.when(menuDao.findAll())
            .thenReturn(menus);

        Mockito.when(menuProductDao.findAllByMenuId(Mockito.anyLong()))
            .thenReturn(Arrays.asList(menuProduct));

        // when
        List<Menu> actual = menuService.list();

        // givne
        assertThat(actual).isEqualTo(menus);
    }

    private Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();

        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    private MenuProduct createMenuProduct(Long seq, Long productId, long quantity, Long menuId) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        menuProduct.setMenuId(menuId);

        return menuProduct;

    }
}