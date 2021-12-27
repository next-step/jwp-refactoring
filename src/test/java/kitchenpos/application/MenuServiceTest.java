package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRequest;
import kitchenpos.domain.MenuProductResponse;
import kitchenpos.domain.MenuRequest;
import kitchenpos.domain.MenuResponse;
import kitchenpos.domain.Product;
import kitchenpos.exception.KitchenposException;
import kitchenpos.exception.KitchenposNotFoundException;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    private MenuGroup menuGroup;
    private Menu menu;
    private Product product;
    private MenuProduct menuProduct;
    private MenuProductRequest menuProductRequest;

    @BeforeEach
    void setUp() {
        menuProductRequest = new MenuProductRequest(1L, 2);
        product = new Product(1L, "product", BigDecimal.ONE);
        menuProduct = new MenuProduct(1L, menu, product, 2);
        menuGroup = new MenuGroup(1L, "menuGroup");
        menu = new Menu(1L, "menu", BigDecimal.ONE, menuGroup, Arrays.asList(menuProduct));
    }

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        // given
        Mockito.when(menuGroupDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(menuGroup));

        Mockito.when(productDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(product));

        Mockito.when(menuDao.save(Mockito.any()))
            .thenReturn(menu);

        MenuRequest request = new MenuRequest("menu", BigDecimal.ONE, 1L, Arrays.asList(menuProductRequest));

        // when
        MenuResponse actual = menuService.create(request);

        // then
        assertAll(
            () -> assertThat(actual.getMenuProducts()).hasSize(1),
            () -> assertThat(actual.getMenuProducts().get(0).getMenuId()).isEqualTo(1L)
        );
    }

    @DisplayName("메뉴 상품 가격의 합과 입력받은 가격 비교하여 입력받은 가격이 더 크면 에러")
    @Test
    void createErrorWhenPriceIsBiggerThanSum() {
        // given
        Mockito.when(menuGroupDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(menuGroup));

        Mockito.when(productDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(product));

        MenuRequest request =
            new MenuRequest("name", BigDecimal.valueOf(3), 1L, Arrays.asList(menuProductRequest));

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> menuService.create(request))
            .withMessage("각 상품 가격의 합보다 많은 가격입니다.");
    }

    @DisplayName("메뉴 상품이 없을 시 에러")
    @Test
    void createErrorWhenProductNotExists() {
        // given
        Mockito.when(menuGroupDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(menuGroup));

        Mockito.when(productDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.empty());

        MenuRequest request =
            new MenuRequest("name", BigDecimal.ONE, 1L, Arrays.asList(menuProductRequest));
        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("메뉴 상품 가격이 null이면 에러")
    @Test
    void createErrorWhenPriceIsNull() {
        MenuRequest menu = new MenuRequest("name", null, 1L, Arrays.asList(menuProductRequest));

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 상품 가격이 0 미만이면 에러")
    @Test
    void createErrorWhenPriceIsLessThanZero() {
        MenuRequest menu = new MenuRequest("name", BigDecimal.valueOf(-1), 1L, Arrays.asList(menuProductRequest));

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 에러 발생")
    @Test
    void createErrorWhenMenuGroupNotExists() {
        // given
        Mockito.when(menuGroupDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.empty());

        MenuRequest menu = new MenuRequest("name", BigDecimal.ONE, 1L, Arrays.asList(menuProductRequest));

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 조회")
    @Test
    void list() {
        // given
        Menu menu1 = new Menu(1L, "name1", BigDecimal.ONE, menuGroup, Arrays.asList(menuProduct));
        Menu menu2 = new Menu(2L, "name2", BigDecimal.ONE, menuGroup, Arrays.asList(menuProduct));
        List<Menu> menus = Arrays.asList(menu1, menu2);
        Mockito.when(menuDao.findAll())
            .thenReturn(menus);

        // when
        List<MenuResponse> actual = menuService.list();

        // given
        assertAll(
            () -> assertThat(actual.get(0).getMenuProducts()).hasSize(1),
            () -> assertThat(actual.get(0).getMenuProducts().get(0)).isEqualTo(MenuProductResponse.from(menuProduct))
        );
    }
}