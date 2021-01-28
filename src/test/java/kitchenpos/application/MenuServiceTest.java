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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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

    private Menu menu;

    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        menuProduct = new MenuProduct(1L, 2);
        menu = new Menu("메뉴", new BigDecimal(200), 1L, Collections.singletonList(menuProduct));
    }

    @DisplayName("메뉴 생성")
    @Test
    void createMenu() {
        // given
        Product product = new Product(1L, "상품", new BigDecimal(100));

        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(product));
        when(menuDao.save(menu)).thenReturn(menu);
        when(menuProductDao.save(menuProduct)).thenReturn(menuProduct);

        // when
        Menu createdMenu = menuService.create(menu);

        // then
        assertThat(createdMenu.getId()).isEqualTo(menu.getId());
        assertThat(createdMenu.getName()).isEqualTo(menu.getName());
        assertThat(createdMenu.getPrice()).isEqualTo(menu.getPrice());
        assertThat(createdMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        assertThat(createdMenu.getMenuProducts()).isEqualTo(menu.getMenuProducts());
    }

    @DisplayName("메뉴의 가격은 0원 이상이여야 함")
    @Test
    void priceException() {
        // given
        menu.setPrice(new BigDecimal(-1));

        // when then
        assertThrows(IllegalArgumentException.class, () -> {
            menuService.create(menu);
        });
    }

    @DisplayName("메뉴의 가격은 메뉴에 속한 상품금액의 합보다 작거나 같아야 함")
    @Test
    void priceOverException() {
        // given
        Product product = new Product(1L, "상품", new BigDecimal(50));

        when(menuGroupDao.existsById(1L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(product));

        // when / then
        assertThrows(IllegalArgumentException.class, () -> {
            menuService.create(menu);
        });
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void selectMenu() {
        // given
        when(menuDao.findAll()).thenReturn(Collections.singletonList(menu));

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
