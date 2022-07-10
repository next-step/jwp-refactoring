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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

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

    private MenuService menuService;

    @BeforeEach
    public void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("메뉴가 생성된다.")
    @Test
    void create() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setPrice(new BigDecimal(10));
        menu.setMenuGroupId(1L);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);

        Product product = new Product();
        product.setPrice(new BigDecimal(10));

        menu.setMenuProducts(Arrays.asList(menuProduct));

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(product));
        given(menuDao.save(menu)).willReturn(menu);

        Menu result = menuService.create(menu);

        assertAll(
                () -> assertNotNull(result.getId()),
                () -> assertThat(result.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("가격이 입력되지 않는 경우 메뉴를 생성할 수 없다.")
    @Test
    void createWithNullPrice() {
        Menu menu = new Menu();
        menu.setId(1L);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0보다 작은 경우 메뉴를 생성할 수 없다.")
    @Test
    void createWithNegativePrice() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setPrice(new BigDecimal(-1));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 포함된 메뉴그룹ID가 존재하지 않으면 메뉴를 생성할 수 없다.")
    @Test
    void createWithUnavailableMenuGroupId() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setPrice(new BigDecimal(10));
        menu.setMenuGroupId(1L);

        given(menuGroupDao.existsById(anyLong())).willReturn(false);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 포함된 각 상품의 (상품 * 수량) 합이 메뉴 가격보다 작은 경우 메뉴를 생성할 수 없다.")
    @Test
    void createWithInvalidProductPrice() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setPrice(new BigDecimal(10000));
        menu.setMenuGroupId(1L);

        MenuProduct firstMenuProduct = new MenuProduct();
        firstMenuProduct.setProductId(1L);
        firstMenuProduct.setQuantity(1);

        MenuProduct secondMenuProduct = new MenuProduct();
        secondMenuProduct.setProductId(2L);
        secondMenuProduct.setQuantity(2);

        Product firstProduct = new Product();
        firstProduct.setPrice(new BigDecimal(10));

        Product secondProduct = new Product();
        secondProduct.setPrice(new BigDecimal(20));

        menu.setMenuProducts(Arrays.asList(firstMenuProduct, secondMenuProduct));

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(firstProduct));
        given(productDao.findById(2L)).willReturn(Optional.of(secondProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
