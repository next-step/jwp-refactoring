package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.graalvm.compiler.nodes.calc.IntegerDivRemNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
    private Product newProduct;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        newProduct = new Product();
        newProduct.setId(1L);
        newProduct.setName("강정치킨");
        newProduct.setPrice(new BigDecimal(17000));

        menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);

        menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드+후라이드");
        menu.setPrice(new BigDecimal(34000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(menuProduct));
    }

    @Test
    @DisplayName("메뉴등록")
    void create() {
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(newProduct));
        when(menuDao.save(any())).thenReturn(menu);
        when(menuProductDao.save(any())).thenReturn(menuProduct);

        assertThat(menuService.create(menu)).isNotNull();
    }

    @Test
    @DisplayName("메뉴등록시 메뉴그룹이 존재하지 않으면 등록 할 수 없음")
    void callExceptionNotMenuGroup() {
        when(menuGroupDao.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> {
            menuService.create(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴조회")
    void list() {
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu));
        when(menuProductDao.findAllByMenuId(any())).thenReturn(Arrays.asList(menuProduct));

        assertThat(menuService.list()).isNotNull();
    }
}
