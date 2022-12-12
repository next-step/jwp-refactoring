package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private MenuService menuService;

    @DisplayName("가격이 비어있거나 0 미만인 경우 메뉴를 등록할 수 없다")
    @Test
    void menu1() {
        assertThatThrownBy(() -> menuService.create(new Menu(null, null, null, null)))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> menuService.create(new Menu(null, BigDecimal.valueOf(-10), null, null)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴그룹id 인 경우 등록할 수 없다")
    @Test
    void menu2() {
        when(menuGroupDao.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> menuService.create(new Menu(null, BigDecimal.valueOf(10), 2L, null)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 상품이어야 한다")
    @Test
    void menu3() {
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.empty());
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(), new MenuProduct());

        assertThatThrownBy(() -> menuService.create(new Menu(null, BigDecimal.valueOf(10), 2L, menuProducts)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 메뉴상품 총금액합보다 작아야한다")
    @Test
    void menu4() {
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(eq(1L))).thenReturn(Optional.of(new Product("1", BigDecimal.valueOf(200))));
        when(productDao.findById(eq(2L))).thenReturn(Optional.of(new Product("2", BigDecimal.valueOf(100))));
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(1L, 3), new MenuProduct(2L, 1));

        // 가격은 200*3 + 100*1 = 700 보다 같거나 작아야한다
        assertThatThrownBy(() -> menuService.create(new Menu(null, BigDecimal.valueOf(701), 2L, menuProducts)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
