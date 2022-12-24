package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void 메뉴를_등록할_수_있다() {
        Menu menu = new Menu(1L, "name", BigDecimal.valueOf(1000), 2L,
            Collections.singletonList(new MenuProduct(1L, 1L, 2L, 1)));
        when(menuGroupDao.existsById(2L)).thenReturn(true);
        when(productDao.findById(2L)).thenReturn(
            Optional.of(new Product(2L, "name", BigDecimal.valueOf(1000))));
        when(menuDao.save(menu)).thenReturn(menu);

        Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu).isEqualTo(menu);
    }

    @Test
    void 메뉴그룹이_중복되는_경우_메뉴를_등록할_수_없다() {
        Menu menu = new Menu(1L, "name", BigDecimal.valueOf(1000), 2L,
            Collections.singletonList(new MenuProduct(1L, 1L, 2L, 1)));
        when(menuGroupDao.existsById(2L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
    }

    @Test
    void 메뉴의_가격이_0원_미만이면_메뉴를_등록할_수_없다() {
        Menu menu = new Menu(1L, "name", BigDecimal.valueOf(-1), 2L,
            Collections.singletonList(new MenuProduct(1L, 1L, 2L, 1)));

        assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
    }

    @Test
    void 메뉴_상품의_총합이_0원_미만이면_메뉴를_등록할_수_없다() {
        Menu menu = new Menu(1L, "name", BigDecimal.valueOf(1000), 2L,
            Collections.singletonList(new MenuProduct(1L, 1L, 2L, 1)));
        when(menuGroupDao.existsById(2L)).thenReturn(true);
        when(productDao.findById(2L)).thenReturn(
            Optional.of(new Product(2L, "name", BigDecimal.valueOf(-1))));

        assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
    }

    @Test
    void 등록한_메뉴를_조회할_수_있다() {
        Menu menu = new Menu(1L, "name", BigDecimal.valueOf(1000), 2L, Collections.emptyList());
        Menu menu2 = new Menu(2L, "name2", BigDecimal.valueOf(1000), 2L, Collections.emptyList());
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu, menu2));

        List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(2);
        assertThat(menus).contains(menu, menu2);
    }
}