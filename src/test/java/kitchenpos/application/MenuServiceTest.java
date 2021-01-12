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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
@DisplayName("메뉴에 대한 비즈니스 로직")
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

    private Menu menu;

    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);

        menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드+후라이드");
        menu.setMenuGroupId(1L);
        menu.setPrice(new BigDecimal(19000));
        menu.setMenuProducts(Arrays.asList(menuProduct));
    }

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void create() {
        // given
        Product product = new Product();
        product.setId(1L);
        product.setName("후라이드");
        product.setPrice(new BigDecimal(10000));

        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(product));
        when(menuDao.save(menu)).thenReturn(menu);
        when(menuProductDao.save(menuProduct)).thenReturn(menuProduct);

        // when
        Menu actual = menuService.create(this.menu);

        // then
        assertThat(actual.getId()).isEqualTo(menu.getId());
        assertThat(actual.getName()).isEqualTo(menu.getName());
        assertThat(actual.getPrice()).isEqualTo(menu.getPrice());
        assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        assertThat(actual.getMenuProducts()).isEqualTo(menu.getMenuProducts());
    }

    @DisplayName("가격은 필수 정보이며 0 이상의 숫자여야 한다.")
    @Test
    void priceRange() {
        // given
        menu.setPrice(null);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
        menu.setPrice(new BigDecimal(-1));
        assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
    }

    @DisplayName("메뉴 가격이 메뉴 상품들의 가격 총합보다 비쌀 수 없다.")
    @Test
    void priceOverSum() {
        // given
        Product product = new Product();
        product.setId(1L);
        product.setName("후라이드");
        product.setPrice(new BigDecimal(5000));

        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(product));

        // when / then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu));

        // when
        List<Menu> list = menuService.list();

        // then
        assertThat(list.get(0).getId()).isEqualTo(menu.getId());
        assertThat(list.get(0).getName()).isEqualTo(menu.getName());
        assertThat(list.get(0).getPrice()).isEqualTo(menu.getPrice());
        assertThat(list.get(0).getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        assertThat(list.get(0).getMenuProducts()).isEqualTo(menu.getMenuProducts());
    }

}
