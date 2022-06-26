package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
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

    private Product chicken;
    private Product ham;
    private MenuProduct chicken_menuProduct;
    private MenuProduct ham_menuProduct;

    @BeforeEach
    public void init() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        chicken = new Product();
        chicken.setPrice(BigDecimal.valueOf(5000));
        chicken_menuProduct = new MenuProduct();
        chicken_menuProduct.setProductId(1L);
        chicken_menuProduct.setQuantity(1);

        ham = new Product();
        ham.setPrice(BigDecimal.valueOf(4000));
        ham_menuProduct = new MenuProduct();
        ham_menuProduct.setProductId(2L);
        ham_menuProduct.setQuantity(1);


    }

    @Test
    @DisplayName("메뉴 생성 정상로직")
    void createMenuHappyCase() {
        //given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuProducts(Arrays.asList(chicken_menuProduct, ham_menuProduct));

        when(productDao.findById(1L)).thenReturn(Optional.of(chicken));
        when(productDao.findById(2L)).thenReturn(Optional.of(ham));
        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);
        when(menuDao.save(menu)).thenReturn(menu);

        //when
        Menu menu_created = menuService.create(menu);

        //then
        assertAll(
            () -> assertThat(menu_created.getPrice().toString()).isEqualTo(String.valueOf(1000)),
            () -> assertThat(menu_created.getMenuProducts()).hasSize(2)
        );

    }

    @Test
    @DisplayName("음수의 가격 및 Null로 메뉴 생성시 에러 발생")
    void createWithMinusPriceThrowError() {
        //given
        Menu menu_minusPrice = new Menu();
        menu_minusPrice.setPrice(BigDecimal.valueOf(-1));

        Menu menu_nullPrice = new Menu();
        menu_minusPrice.setPrice(null);

        //when
        assertAll(
            () -> assertThatThrownBy(() -> menuService.create(menu_minusPrice))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> menuService.create(menu_nullPrice))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("어느 메뉴그룹에도 속해있지 않으면 에러발생")
    void createWithNoMenuGroupThrowError() {
        //given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(1000));

        //when & then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 속해있는 상품들의 합보다 가격이 크면 에러 발생")
    void createWithTooMuchPriceThrowError() {
        //given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuProducts(Arrays.asList(chicken_menuProduct, ham_menuProduct));

        when(productDao.findById(1L)).thenReturn(Optional.of(chicken));
        when(productDao.findById(2L)).thenReturn(Optional.of(ham));
        when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

}