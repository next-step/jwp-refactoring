package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.ProductServiceTest.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 서비스")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private MenuService menuService;

    private Menu 매콤치킨단품;
    private Menu 매콤치즈볼세트;
    private Product 매콤치킨;
    private Product 치즈볼;
    private MenuProduct 매콤치킨구성;
    private MenuProduct 치즈볼구성;

    @BeforeEach
    void setUp() {
        매콤치킨단품 = createMenu(1L, "매콤치킨단품", BigDecimal.valueOf(13000), 1L);
        매콤치즈볼세트 = createMenu(2L, "매콤치즈볼세트", BigDecimal.valueOf(15000), 1L);

        매콤치킨 = createProduct(1L, "매콤치킨", BigDecimal.valueOf(13000));
        치즈볼 = createProduct(2L, "치즈볼", BigDecimal.valueOf(2000));

        매콤치킨구성 = createMenuProduct(매콤치킨.getId(), 1L);
        치즈볼구성 = createMenuProduct(치즈볼.getId(), 2L);

        매콤치킨단품.setMenuProducts(Collections.singletonList(매콤치킨구성));
        매콤치즈볼세트.setMenuProducts(Arrays.asList(매콤치킨구성, 치즈볼구성));
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        when(menuGroupDao.existsById(anyLong())).thenReturn(true);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(매콤치킨));
        when(menuDao.save(any())).thenReturn(매콤치킨단품);
        when(menuProductDao.save(any())).thenReturn(매콤치킨구성);

        Menu menu = menuService.create(매콤치킨단품);

        verify(menuGroupDao, times(1)).existsById(anyLong());
        verify(productDao, times(1)).findById(anyLong());
        verify(menuDao, times(1)).save(any(Menu.class));
        verify(menuProductDao, times(1)).save(any(MenuProduct.class));
        assertThat(menu)
                .extracting("name", "price", "menuGroupId")
                .containsExactly(매콤치킨단품.getName(), 매콤치킨단품.getPrice(), 매콤치킨단품.getMenuGroupId());
    }

    @Test
    @DisplayName("메뉴의 가격이 상품 가격의 합보다 큰 경우 예외가 발생한다.")
    void validateProductsSum() {
        BigDecimal overPriceSum = BigDecimal.valueOf(18000);
        Menu 비싼매콤치즈볼세트 = createMenu(3L, "비싼매콤치즈볼세트", overPriceSum, 1L);
        비싼매콤치즈볼세트.setMenuProducts(Arrays.asList(매콤치킨구성, 치즈볼구성)); // 13000 + 2000

        when(menuGroupDao.existsById(anyLong())).thenReturn(true);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(매콤치킨));
        when(productDao.findById(anyLong())).thenReturn(Optional.of(치즈볼));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(비싼매콤치즈볼세트));
    }

    @Test
    @DisplayName("메뉴의 가격이 null인 경우 예외가 발생한다.")
    void validatePriceNull() {
        Menu menu = createMenu(2L, "통통치킨한마리", null, 1L);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("메뉴의 가격이 0원 미만인 경우 예외가 발생한다.")
    void validateMinPrice() {
        BigDecimal invalidPrice = BigDecimal.valueOf(-1);
        Menu menu = createMenu(2L, "통통치킨한마리", invalidPrice, 1L);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("메뉴 그룹에 속하지 않는 메뉴인 경우 예외가 발생한다.")
    void validateMenuGroup() {
        Menu menu = createMenu(2L, "통통치킨한마리", BigDecimal.valueOf(10000), 1L);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
        verify(menuGroupDao, times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("메뉴에 속한 상품이 존재하지 않는 경우 예외가 발생한다.")
    void validateProduct() {
        Menu menu = createMenu(2L, "통통치킨한마리", BigDecimal.valueOf(10000), 1L);
        MenuProduct menuProduct = createMenuProduct(1L, 1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        when(menuGroupDao.existsById(anyLong())).thenReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menu));
        verify(menuGroupDao, times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        when(menuDao.findAll()).thenReturn(Collections.singletonList(매콤치킨단품));

        List<Menu> menus = menuService.list();

        verify(menuDao, times(1)).findAll();
        assertThat(menus).hasSize(1);
    }

    private Menu createMenu(Long id, String name, BigDecimal price, long menuGroupId) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    private MenuProduct createMenuProduct(long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}