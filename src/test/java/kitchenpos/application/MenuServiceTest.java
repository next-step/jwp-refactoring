package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
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
        MenuProduct menuProduct = new MenuProduct(1L, 2);
        Mockito.when(menuGroupDao.existsById(Mockito.anyLong()))
            .thenReturn(true);

        Product product = new Product(1L, "name", BigDecimal.TEN);
        Mockito.when(productDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(product));

        MenuProduct expectedMenuProduct = new MenuProduct(2L, 2L, 2L, 2);
        Mockito.when(menuProductDao.save(Mockito.any()))
            .thenReturn(expectedMenuProduct);

        Menu expectedMenu = new Menu(1L, "name", BigDecimal.ONE, 1L, Collections.emptyList());
        Mockito.when(menuDao.save(Mockito.any()))
            .thenReturn(expectedMenu);

        Menu menu = new Menu("name", BigDecimal.ONE, 1L, Arrays.asList(menuProduct));

        // when
        Menu actual = menuService.create(menu);

        // then
        assertAll(
            () -> assertThat(actual.getMenuProducts()).hasSize(1),
            () -> assertThat(actual.getMenuProducts().get(0).getMenuId()).isEqualTo(2L)
        );
    }

    @DisplayName("메뉴 상품 가격의 합과 입력받은 가격 비교하여 입력받은 가격이 더 크면 에러")
    @Test
    void createErrorWhenPriceIsBiggerThanSum() {
        // given
        MenuProduct menuProduct = new MenuProduct(1L, 2);
        Mockito.when(menuGroupDao.existsById(Mockito.anyLong()))
            .thenReturn(true);

        Product product = new Product(1L, "name", BigDecimal.TEN);
        Mockito.when(productDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(product));

        Menu menu = new Menu("name", BigDecimal.valueOf(21), 1L, Arrays.asList(menuProduct));

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 상품 가격의 합과 입력받은 가격 비교하여 입력받은 가격이 더 크면 에러")
    @Test
    void createErrorWhenProductNotExists() {
        // given
        MenuProduct menuProduct = new MenuProduct(1L, 2);
        Mockito.when(menuGroupDao.existsById(Mockito.anyLong()))
            .thenReturn(true);

        Mockito.when(productDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.empty());

        Menu menu = new Menu("name", BigDecimal.ONE, 1L, Arrays.asList(menuProduct));

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 상품 가격이 null이면 에러")
    @Test
    void createErrorWhenPriceIsNull() {
        MenuProduct menuProduct = new MenuProduct(1L, 2);
        Menu menu = new Menu("name", null, 1L, Arrays.asList(menuProduct));

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 상품 가격이 0 미만이면 에러")
    @Test
    void createErrorWhenPriceIsLessThanZero() {
        MenuProduct menuProduct = new MenuProduct(1L, 2);
        Menu menu = new Menu("name", BigDecimal.valueOf(-1), 1L, Arrays.asList(menuProduct));

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 에러 발생")
    @Test
    void createErrorWhenMenuGroupNotExists() {
        // given
        MenuProduct menuProduct = new MenuProduct(1L, 2);
        Mockito.when(menuGroupDao.existsById(Mockito.anyLong()))
            .thenReturn(false);

        Menu menu = new Menu("name", BigDecimal.ONE, 1L, Arrays.asList(menuProduct));

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @Test
    void list() {
        // given
        MenuProduct menuProduct = new MenuProduct(1L, 2);

        Menu menu1 = new Menu(1L, "name1", BigDecimal.ONE, 1L, Collections.emptyList());
        Menu menu2 = new Menu(2L, "name2", BigDecimal.TEN, 1L, Collections.emptyList());
        List<Menu> menus = Arrays.asList(menu1, menu2);
        Mockito.when(menuDao.findAll())
            .thenReturn(menus);

        Mockito.when(menuProductDao.findAllByMenuId(Mockito.anyLong()))
            .thenReturn(Arrays.asList(menuProduct));

        // when
        List<Menu> actual = menuService.list();

        // given
        assertAll(
            () -> assertThat(actual.get(0).getMenuProducts()).hasSize(1),
            () -> assertThat(actual.get(0).getMenuProducts().get(0)).isEqualTo(menuProduct)
        );
        assertThat(actual).isEqualTo(menus);
    }
}