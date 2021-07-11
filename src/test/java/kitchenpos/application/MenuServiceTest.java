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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 관련 기능 테스트")
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

    private Product 짜장면;
    private Product 탕수육;

    private MenuProduct 짜장면_메뉴;
    private MenuProduct 탕수육_메뉴;

    private Menu 메뉴;

    @BeforeEach
    void setUp() {
        짜장면 = new Product();
        짜장면.setId(1L);
        짜장면.setName("짜장면");
        짜장면.setPrice(new BigDecimal(7000));

        탕수육 = new Product();
        탕수육.setId(2L);
        탕수육.setName("탕수육");
        탕수육.setPrice(new BigDecimal(12000));

        짜장면_메뉴 = new MenuProduct();
        짜장면_메뉴.setSeq(1L);
        짜장면_메뉴.setMenuId(1L);
        짜장면_메뉴.setProductId(1L);
        짜장면_메뉴.setQuantity(1);

        탕수육_메뉴 = new MenuProduct();
        탕수육_메뉴.setSeq(2L);
        탕수육_메뉴.setMenuId(1L);
        탕수육_메뉴.setProductId(2L);
        탕수육_메뉴.setQuantity(1);

        메뉴 = new Menu();
        메뉴.setId(1L);
        메뉴.setMenuGroupId(1L);
        메뉴.setMenuProducts(Arrays.asList(짜장면_메뉴, 탕수육_메뉴));
        메뉴.setName("짜장면 탕수육 세트");
        메뉴.setPrice(new BigDecimal(19000));
    }

    @Test
    void 메뉴_등록_기능() {

        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(짜장면));
        when(productDao.findById(2L)).thenReturn(Optional.of(탕수육));

        when(menuDao.save(메뉴)).thenReturn(메뉴);
        when(menuProductDao.save(짜장면_메뉴)).thenReturn(짜장면_메뉴);
        when(menuProductDao.save(탕수육_메뉴)).thenReturn(탕수육_메뉴);

        Menu expected = menuService.create(메뉴);

        assertThat(expected.getId()).isEqualTo(메뉴.getId());
        assertThat(expected.getName()).isEqualTo(메뉴.getName());
        assertThat(expected.getMenuGroupId()).isEqualTo(메뉴.getMenuGroupId());
        assertThat(expected.getPrice()).isEqualTo(메뉴.getPrice());

        assertThat(expected.getMenuProducts().size()).isEqualTo(2);
    }

    @Test
    void 존재하지않는_메뉴그룹_아이디_등록_요청_시_에러_발생() {
        when(menuGroupDao.existsById(1L)).thenReturn(false);
        assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지않는_상품을_메뉴_상품에_등록_요청_시_에러_발생() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(100L);
        menuProduct.setQuantity(1);
        menuProduct.setProductId(100L);
        menuProduct.setMenuId(1L);
        메뉴.setMenuProducts(Arrays.asList(menuProduct));
        when(menuGroupDao.existsById(1L)).thenReturn(true);

        assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_메뉴_상품들의_가격합보다_클_경우_에러_발생() {
        메뉴.setPrice(new BigDecimal(20000));

        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(짜장면));
        when(productDao.findById(2L)).thenReturn(Optional.of(탕수육));

        assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_조회() {
        when(menuDao.findAll()).thenReturn(Arrays.asList(메뉴));
        when(menuProductDao.findAllByMenuId(1L)).thenReturn(Arrays.asList(짜장면_메뉴, 탕수육_메뉴));

        List<Menu> menus = menuService.list();

        assertThat(menus.size()).isEqualTo(1);
        Menu expected = menus.get(0);
        assertThat(expected.getId()).isEqualTo(메뉴.getId());
        assertThat(expected.getName()).isEqualTo(메뉴.getName());
        assertThat(expected.getMenuGroupId()).isEqualTo(메뉴.getMenuGroupId());
        assertThat(expected.getPrice()).isEqualTo(메뉴.getPrice());
        assertThat(expected.getMenuProducts().size()).isEqualTo(2);
    }
}
