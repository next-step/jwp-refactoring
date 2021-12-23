package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuDao menuDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuProductDao menuProductDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴를 등록한다.")
    @Test
    void saveMenu() {
        final Product product1 = new Product(1L, "상품", new BigDecimal("10000"));
        final Product product2 = new Product(2L, "상품2", new BigDecimal("10000"));
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 2);
        final Menu menu = new Menu(1L, "메뉴", new BigDecimal("10000"), 1L, Collections.singletonList(menuProduct));

        given(menuGroupDao.existsById(anyLong())).willReturn(true);
        given(productDao.findById(anyLong())).willReturn(Optional.of(product1));
        given(menuDao.save(any())).willReturn(menu);
        given(menuProductDao.save(any())).willReturn(menuProduct);

        final Menu actual = menuService.create(menu);

        assertThat(menu).isEqualTo(actual);
    }

    @DisplayName("메뉴들을 조회한다.")
    @Test
    void findMenus() {
        final MenuProduct menuProduct1 = new MenuProduct(1L, 1L, 1L, 2);
        final MenuProduct menuProduct2 = new MenuProduct(2L, 1L, 2L, 3);
        final List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final Menu menu = new Menu(1L, "메뉴", new BigDecimal("10000"), 1L, menuProducts);

        given(menuDao.findAll()).willReturn(Collections.singletonList(menu));
        given(menuProductDao.findAllByMenuId(anyLong())).willReturn(menuProducts);

        final List<Menu> actual = menuService.list();

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getMenuProducts()).hasSize(2)
        );
    }
}
