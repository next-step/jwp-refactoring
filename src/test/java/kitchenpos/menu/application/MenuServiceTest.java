package kitchenpos.menu.application;

import kitchenpos.application.MenuService;
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
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
        menuProduct = new MenuProduct(1L, 2);
        menu = new Menu("후라이드+후라이드", new BigDecimal(19000), 1L, Arrays.asList(menuProduct));
    }

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void createMenu() {
        // given
        Product product = new Product("후라이드", new BigDecimal(10000));

        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(product));
        when(menuDao.save(menu)).thenReturn(menu);
        when(menuProductDao.save(menuProduct)).thenReturn(menuProduct);

        // when
        Menu createdMenu = menuService.create(this.menu);

        // then
        assertThat(createdMenu.getId()).isEqualTo(menu.getId());
        assertThat(createdMenu.getName()).isEqualTo(menu.getName());
        assertThat(createdMenu.getPrice()).isEqualTo(menu.getPrice());
        assertThat(createdMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        assertThat(createdMenu.getMenuProducts()).isEqualTo(menu.getMenuProducts());
    }

    @DisplayName("메뉴의 가격은 0원 이상이어야 한다.")
    @Test
    void createMenuPriceException() {
        // given
        menu.setPrice(new BigDecimal(-1000));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            menuService.create(menu);
        });
    }

    @DisplayName("메뉴의 가격이 메뉴에 속하는 상품 가격의 합보다 크지 않아야 한다.")
    @Test
    void createMenuPriceOverException() {
        // given
        Product product = new Product("후라이드", new BigDecimal(5000));

        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(product));

        // when / then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            menuService.create(menu);
        });
    }

    @Test
    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    void findAllMenus() {
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
