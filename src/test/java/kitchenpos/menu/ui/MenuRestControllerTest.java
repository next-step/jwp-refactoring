package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.exceptions.InputMenuDataErrorCode;
import kitchenpos.menu.exceptions.InputMenuDataException;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("메뉴 컨트롤러 테스트")
@ExtendWith(MockitoExtension.class)
class MenuRestControllerTest {

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
    @DisplayName("메뉴를 등록한다.")
    void saveMenuTest() {
        //given
        Menu menu = mock(Menu.class);
        when(menu.getPrice())
                .thenReturn(new BigDecimal("10000"));
        when(menuGroupDao.existsById(anyLong()))
                .thenReturn(true);

        MenuProduct menuProduct = mock(MenuProduct.class);
        when(menu.getMenuProducts())
                .thenReturn(Arrays.asList(menuProduct));
        when(menuProduct.getQuantity())
                .thenReturn(1L);

        Product product = mock(Product.class);
        when(product.getPrice())
                .thenReturn(new BigDecimal("10000"));
        when(productDao.findById(anyLong()))
                .thenReturn(Optional.of(product));

        Menu savedMenu = mock(Menu.class);
        when(savedMenu.getPrice())
                .thenReturn(new BigDecimal("10000"));

        when(menuDao.save(menu)).thenReturn(savedMenu);
        //when
        menuService.create(menu);

        //then
        verify(menuDao).save(menu);
        verify(productDao).findById(anyLong());
    }

    @Test
    @DisplayName("메뉴 조회한다.")
    void findMenusTest() {
        //given
        Menu menu = mock(Menu.class);
        when(menu.getPrice())
                .thenReturn(new BigDecimal("1000"));
        when(menuDao.findAll())
                .thenReturn(Arrays.asList(menu));
        //when
        List<Menu> menus = menuService.list();
        //then
        assertThat(menus).contains(menu);
        verify(menuDao).findAll();
    }

    @Test
    @DisplayName("잘못된 가격의 메뉴를 등록하면 에러처리한다.")
    void saveWrongPriceMenuTest() {
        //given
        Menu menu = mock(Menu.class);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
                    new Menu(1L, "치킨", new BigDecimal(-100), 1L);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.IT_CAN_NOT_INPUT_MENU_PRICE_LESS_THAN_ZERO.errorMessage());
    }

    @Test
    @DisplayName("menuGroupId를 입력하지 않았을 때 에러처리한다.")
    void saveEmptyMenuGroupIdPriceMenuTest() {
        //given
        Menu menu = mock(Menu.class);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
                    new Menu(1L, "치킨", new BigDecimal(100), null);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.YOU_MUST_INPUT_MENU_GROUP_ID.errorMessage());
    }

    @Test
    @DisplayName("menuGroupId가 0보다 작을 경우 에러처리한다.")
    void saveWrongMenuGroupIdPriceMenuTest() {
        //given
        Menu menu = mock(Menu.class);
        //when
        //then
        Assertions.assertThatThrownBy(() -> {
                    new Menu(1L, "치킨", new BigDecimal(100), -2L);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.THE_MENU_GROUP_ID_IS_LESS_THAN_ZERO.errorMessage());
    }

    @Test
    @DisplayName("등록되지 않는 상품은 메뉴로 등록할수 없다.")
    void saveMenuNotRegisteredProduct() {
        //given
        Menu menu = mock(Menu.class);
        when(menu.getPrice())
                .thenReturn(new BigDecimal(1000));
        when(menuGroupDao.existsById(anyLong()))
                .thenReturn(true);
        MenuProduct menuProduct = mock(MenuProduct.class);
        when(menu.getMenuProducts())
                .thenReturn(Arrays.asList(menuProduct));

        //when
        when(productDao.findById(anyLong()))
                .thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            menuService.create(menu);
        }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.THE_PRODUCT_IS_NOT_REGISTERED.errorMessage());
    }
}
