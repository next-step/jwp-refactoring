package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.menugroup.repository.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 테스트")
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    private Menu menu;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach

    void setup() {
        menu = new Menu();
        menu.setId(1L);
        menu.setMenuGroupId(1L);
        menu.setName("순대국");
        menu.setPrice(BigDecimal.valueOf(8000));
        menu.setMenuProducts(Arrays.asList(new MenuProduct()));
    }

    @DisplayName("사용자는 메뉴를 만들 수 있다.")
    @Test
    void create() {
        // given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(8);

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1000));

        menu.setMenuProducts(Arrays.asList(menuProduct));
        // when
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(product));
        when(menuDao.save(menu)).thenReturn(menu);
        Menu createdMenu = menuService.create(this.menu);
        // then
        assertThat(createdMenu).isNotNull();
        assertThat(createdMenu.getId()).isEqualTo(1L);
    }

    @DisplayName("사용자는 메뉴 리스트를 조회 할 수 있다.")
    @Test
    void findAll() {
        // given

        // when
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu));
        List<Menu> menus = menuService.list();
        // then
        assertThat(menus.size()).isEqualTo(1);
        assertThat(menus.get(0).getId()).isEqualTo(1L);
    }

    @DisplayName("메뉴 가격이 음수 일 수 없다.")
    @Test
    void createFailedByPriceZero() {
        // given
        menu.setPrice(BigDecimal.ZERO);
        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 메뉴 그룹에 반드시 포함 되어야 한다.")
    @Test
    void createFailedByMenuGroup() {
        // given
        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 상품은 반드시 존재해야 한다.")
    @Test
    void createFailedByPrice() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(8);

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(100));

        menu.setMenuProducts(Arrays.asList(menuProduct));
        // when
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(product));
        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }
}