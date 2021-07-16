package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }


    @Test
    public void 메뉴생성_예외_메뉴가격이_개별상품가격보다_큰경우() {
        // given
        Menu menu = mock(Menu.class);
        when(menu.getPrice()).thenReturn(BigDecimal.valueOf(25000));
        when(menuGroupDao.existsById(anyLong())).thenReturn(true);

        MenuProduct menuProduct = mock(MenuProduct.class);
        when(menu.getMenuProducts()).thenReturn(asList(menuProduct));
        when(menuProduct.getQuantity()).thenReturn(1L);

        Product product = mock(Product.class);
        when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(24000));

        // when-then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 메뉴생성_예외_메뉴가격이0보다작은경우() {
        //given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(-1000));

        //when, then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 메뉴생성_예외_메뉴그룹이없는경우() {
        //given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuGroupId(1L);
        given(menuGroupDao.existsById(anyLong())).willReturn(false);

        //when, then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 메뉴생성_성공() {
        //given
        Product 양념치킨 = new Product();
        양념치킨.setId(1L);
        양념치킨.setPrice(BigDecimal.valueOf(13000));
        Product 후라이드치킨 = new Product();
        후라이드치킨.setId(2L);
        후라이드치킨.setPrice(BigDecimal.valueOf(12000));

        MenuProduct 메뉴1_양념 = new MenuProduct();
        메뉴1_양념.setMenuId(1L);
        메뉴1_양념.setProductId(1L);
        메뉴1_양념.setQuantity(1L);

        MenuProduct 메뉴1_후라이드 = new MenuProduct();
        메뉴1_후라이드.setMenuId(1L);
        메뉴1_후라이드.setProductId(2L);
        메뉴1_후라이드.setQuantity(1L);

        Menu 메뉴1 = new Menu();
        메뉴1.setId(1L);
        메뉴1.setPrice(BigDecimal.valueOf(24000));
        메뉴1.setMenuProducts(asList(메뉴1_양념, 메뉴1_후라이드));
        메뉴1.setMenuGroupId(1L);

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(메뉴1_양념.getProductId())).willReturn(Optional.of(양념치킨));
        given(productDao.findById(메뉴1_후라이드.getProductId())).willReturn(Optional.of(후라이드치킨));
        given(menuDao.save(any())).willReturn(메뉴1);
        given(menuProductDao.save(메뉴1_양념)).willReturn(메뉴1_양념);
        given(menuProductDao.save(메뉴1_후라이드)).willReturn(메뉴1_후라이드);

        //when
        Menu savedMenu = menuService.create(메뉴1);

        //then
        assertThat(savedMenu.getId()).isEqualTo(1L);
        assertThat(savedMenu.getPrice()).isEqualTo(BigDecimal.valueOf(24000));
    }

    @Test
    public void 메뉴조회_성공() {
        //given
        Menu menu = mock(Menu.class);
        when(menuDao.findAll()).thenReturn(asList(menu));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).containsExactly(menu);
        verify(menuProductDao).findAllByMenuId(anyLong());
        verify(menu).setMenuProducts(anyList());
    }

}