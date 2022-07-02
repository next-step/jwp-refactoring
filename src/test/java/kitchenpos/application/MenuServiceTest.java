package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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
class MenuServiceTest {
    public static Menu 불고기_새우버거_메뉴 = new Menu();
    public static MenuProduct 불고기버거 = new MenuProduct();
    public static MenuProduct 새우버거 = new MenuProduct();

    static {
        불고기_새우버거_메뉴.setId(1L);
        불고기_새우버거_메뉴.setName("불고기버거 + 새우버거");
        불고기_새우버거_메뉴.setPrice(BigDecimal.valueOf(2000.0));
        불고기_새우버거_메뉴.setMenuGroupId(MenuGroupServiceTest.햄버거_메뉴.getId());

        불고기버거.setProductId(ProductServiceTest.불고기버거.getId());
        불고기버거.setQuantity(1L);

        새우버거.setProductId(ProductServiceTest.새우버거.getId());
        불고기버거.setQuantity(2L);

        불고기_새우버거_메뉴.setMenuProducts(Arrays.asList(불고기버거, 새우버거));
    }

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
    @DisplayName("메뉴 추가")
    void create() {
        // given
        given(menuGroupDao.existsById(any()))
                .willReturn(true);
        given(productDao.findById(any()))
                .willReturn(Optional.of(ProductServiceTest.불고기버거));
        given(menuDao.save(any()))
                .willReturn(불고기_새우버거_메뉴);

        // when
        final Menu 메뉴_생성 = menuService.create(불고기_새우버거_메뉴);
        // then
        assertThat(메뉴_생성).isInstanceOf(Menu.class);
    }

    @Test
    @DisplayName("메뉴 조회")
    void list() {
        // given
        given(menuDao.findAll())
                .willReturn(Arrays.asList(불고기_새우버거_메뉴));

        given(menuProductDao.findAllByMenuId(any()))
                .willReturn(Arrays.asList(불고기버거, 새우버거));
        // when
        final List<Menu> list = menuService.list();
        // then
        assertThat(list).hasSize(1);
    }
}
