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

import static kitchenpos.fixture.MenuFixture.메뉴_데이터_생성;
import static kitchenpos.fixture.MenuFixture.메뉴_요청_데이터_생성;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품_데이터_생성;
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

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        //given
        String name = "menu";
        Long menuGroupId = 1L;
        BigDecimal menuPrice = BigDecimal.valueOf(1000);
        MenuProduct menuProduct = 메뉴상품_데이터_생성(1L, 1L, 2);
        MenuProduct menuProduct2 = 메뉴상품_데이터_생성(2L, 2L, 2);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct, menuProduct2);
        Menu request = 메뉴_요청_데이터_생성(name, menuPrice, menuGroupId, menuProducts);

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(new Product(1L, "product", BigDecimal.valueOf(300))));
        given(productDao.findById(any())).willReturn(Optional.of(new Product(2L, "product", BigDecimal.valueOf(500))));

        Long menuId = 1L;
        given(menuDao.save(any())).willReturn(메뉴_데이터_생성(menuId, name, menuPrice, menuGroupId, menuProducts));

        menuProduct.setMenuId(menuId);
        menuProduct2.setMenuId(menuId);
        given(menuProductDao.save(menuProduct)).willReturn(menuProduct);
        given(menuProductDao.save(menuProduct2)).willReturn(menuProduct2);

        //when
        Menu menu = menuService.create(request);

        //then
        메뉴_데이터_확인(menu, menuId, name, menuGroupId, menuPrice);
        메뉴상품_데이터_확인(menu.getMenuProducts().get(0), 1L, 1L, 1L);
        메뉴상품_데이터_확인(menu.getMenuProducts().get(1), 2L, 1L, 2L);
    }

    @DisplayName("가격이 비어있다면 생성할 수 없다.")
    @Test
    void create_fail_priceNull() {
        //given
        Menu request = createFailRequest(null);

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("가격이 음수면 생성할 수 없다.")
    @Test
    void create_fail_priceNegative() {
        //given
        Menu request = createFailRequest(BigDecimal.valueOf(-1));

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("메뉴묶음이 존재하지 않으면 생성할 수 없다.")
    @Test
    void create_fail_menuGroupNotExists() {
        //given
        Menu request = createFailRequest(BigDecimal.valueOf(1000));

        given(menuGroupDao.existsById(any())).willReturn(false);

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("상품이 존재하지 않으면 생성할 수 없다.")
    @Test
    void create_fail_productNotExists() {
        //given
        Menu request = createFailRequest(BigDecimal.valueOf(1000));

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
        Menu request = createFailRequest(BigDecimal.valueOf(1000));

        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(
                Optional.of(new Product(1L, "product", BigDecimal.valueOf(300))),
                Optional.of(new Product(2L, "product", BigDecimal.valueOf(100))));

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("메뉴와 메뉴상품을 전체 조회한다.")
    @Test
    void list() {
        //given
        Long menuId = 1L;
        String name = "menu";
        BigDecimal price = BigDecimal.valueOf(1000);
        Long menuGroupId = 1L;
        List<MenuProduct> menuProducts = Arrays.asList(메뉴상품_데이터_생성(1L, 1L, 1));

        given(menuDao.findAll()).willReturn(Arrays.asList(메뉴_데이터_생성(menuId, name, price, menuGroupId, menuProducts)));
        given(menuProductDao.findAllByMenuId(menuId)).willReturn(menuProducts);

        //when
        List<Menu> menus = menuService.list();

        //then
        assertEquals(1, menus.size());
        메뉴_데이터_확인(menus.get(0), menuId, name, menuGroupId, price);
    }

    private Menu createFailRequest(BigDecimal menuPrice) {
        String name = "menu";
        Long menuGroupId = 1L;

        Long productId = 1L;
        Long productId2 = 2L;
        MenuProduct menuProduct = new MenuProduct(1L, productId, 2);
        MenuProduct menuProduct2 = new MenuProduct(2L, productId2, 1);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct, menuProduct2);

        return 메뉴_요청_데이터_생성(name, menuPrice, menuGroupId, menuProducts);
    }

    private void 메뉴_데이터_확인(Menu menu, Long id, String name, Long menuGroupId, BigDecimal menuPrice) {
        assertAll(
                () -> assertEquals(id, menu.getId()),
                () -> assertEquals(name, menu.getName()),
                () -> assertEquals(menuPrice, menu.getPrice()),
                () -> assertEquals(menuGroupId, menu.getMenuGroupId()),
                () -> assertThat(menu.getMenuProducts()).isNotEmpty()
        );
    }

    private void 메뉴상품_데이터_확인(MenuProduct menuProduct, Long seq, Long menuId, Long productId) {
        assertAll(
                () -> assertEquals(seq, menuProduct.getSeq()),
                () -> assertEquals(menuId, menuProduct.getMenuId()),
                () -> assertEquals(productId, menuProduct.getProductId())
        );
    }
}