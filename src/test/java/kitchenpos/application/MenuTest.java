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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 관련 기능")
public class MenuTest {
    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    ProductDao productDao;

    @Mock
    MenuProductDao menuProductDao;

    @InjectMocks
    MenuService menuService;

    private Menu menu;
    private Product product;

    @BeforeEach
    void setUp() {
        product = ProductTest.상품_등록(1L, "짜장면", 5000);

        MenuProduct menuProduct = 메뉴_상품_등록(1L, 1L, 1L);

        menu = 메뉴_등록("짜장면", new BigDecimal(5000), 1L, 1L, Arrays.asList(menuProduct));
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        // given
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(product));
        given(menuDao.save(any())).willReturn(menu);

        // when
        Menu createMenu = menuService.create(menu);

        // then
        assertThat(createMenu).isNotNull();
    }

    Menu 메뉴_등록(String name, BigDecimal price, Long id, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName("짜장면");
        menu.setPrice(new BigDecimal(5000));
        menu.setId(1L);
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    MenuProduct 메뉴_상품_등록(Long seq, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
