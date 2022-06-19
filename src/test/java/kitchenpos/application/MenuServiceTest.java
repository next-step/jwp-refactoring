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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private MenuService menuService;

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
    }

    @Test
    void create() {
        //given
        String name = "menu";
        BigDecimal menuPrice = BigDecimal.valueOf(1000);
        Long menuGroupId = 1L;
        Long productId = 1L;
        Long productId2 = 2L;
        MenuProduct menuProduct = new MenuProduct(1L, productId, 2);

        MenuProduct menuProduct2 = new MenuProduct(2L, productId2, 1);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct, menuProduct2);
        Menu request = new Menu(name, menuPrice, menuGroupId, menuProducts);

        given(menuGroupDao.existsById(menuGroupId)).willReturn(true);
        given(productDao.findById(productId)).willReturn(Optional.of(new Product(productId, "product", BigDecimal.valueOf(300))));
        given(productDao.findById(productId2)).willReturn(Optional.of(new Product(productId2, "product", BigDecimal.valueOf(500))));

        Long menuId = 1L;
        given(menuDao.save(any())).willReturn(new Menu(menuId, name, menuPrice, menuGroupId, menuProducts));

        menuProduct.setMenuId(menuId);
        menuProduct2.setMenuId(menuId);
        given(menuProductDao.save(menuProduct)).willReturn(menuProduct);
        given(menuProductDao.save(menuProduct2)).willReturn(menuProduct2);

        //when
        Menu menu = menuService.create(request);

        //then
        assertAll(
                () -> assertEquals(name, menu.getName()),
                () -> assertEquals(menuPrice, menu.getPrice()),
                () -> assertEquals(menuGroupId, menu.getMenuGroupId()),
                () -> assertThat(menu.getMenuProducts()).isNotEmpty()
        );
        List<MenuProduct> savedMenuProducts = menu.getMenuProducts();
        assertAll(
                () -> assertEquals(1L, savedMenuProducts.get(0).getSeq()),
                () -> assertEquals(menuId, savedMenuProducts.get(0).getMenuId()),
                () -> assertEquals(productId, savedMenuProducts.get(0).getProductId()),
                () -> assertEquals(2L, savedMenuProducts.get(1).getSeq()),
                () -> assertEquals(menuId, savedMenuProducts.get(1).getMenuId()),
                () -> assertEquals(productId2, savedMenuProducts.get(1).getProductId())
        );
    }

    @DisplayName("가격이 비어있다면 생성할 수 없다.")
    @Test
    void create_fail_priceNull() {
        //given
        Menu request = createRequest(null);

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("가격이 음수면 생성할 수 없다.")
    @Test
    void create_fail_priceNegative() {
        //given
        Menu request = createRequest(BigDecimal.valueOf(-1));

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("메뉴묶음이 존재하지 않으면 생성할 수 없다.")
    @Test
    void create_fail_menuGroupNotExists() {
        //given
        Menu request = createRequest(BigDecimal.valueOf(1000));

        given(menuGroupDao.existsById(any())).willReturn(false);

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("상품이 존재하지 않으면 생성할 수 없다.")
    @Test
    void create_fail_productNotExists() {
        //given
        Menu request = createRequest(BigDecimal.valueOf(1000));

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(
                Optional.of(new Product(1L, "product", BigDecimal.valueOf(300))),
                Optional.empty());

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("가격이 상품가격의 합보다 크면 생성할 수 없다.")
    @Test
    void create_fail_menuPriceGe() {
        //given
        Menu request = createRequest(BigDecimal.valueOf(1000));

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(
                Optional.of(new Product(1L, "product", BigDecimal.valueOf(300))),
                Optional.of(new Product(2L, "product", BigDecimal.valueOf(100))));

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request));
    }

    @Test
    void list() {
        //given
        Long menuId = 1L;
        String name = "menu";
        BigDecimal price = BigDecimal.valueOf(1000);
        Long menuGroupId = 1L;
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(1L, 1L, 1));

        given(menuDao.findAll()).willReturn(Arrays.asList(new Menu(menuId, name, price, menuGroupId, menuProducts)));
        given(menuProductDao.findAllByMenuId(menuId)).willReturn(menuProducts);

        //when
        List<Menu> menus = menuService.list();

        //then
        assertEquals(1, menus.size());
    }

    private Menu createRequest(BigDecimal menuPrice) {
        String name = "menu";
        Long menuGroupId = 1L;

        Long productId = 1L;
        Long productId2 = 2L;
        MenuProduct menuProduct = new MenuProduct(1L, productId, 2);
        MenuProduct menuProduct2 = new MenuProduct(2L, productId2, 1);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct, menuProduct2);

        return new Menu(name, menuPrice, menuGroupId, menuProducts);
    }
}