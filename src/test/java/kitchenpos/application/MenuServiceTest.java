package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

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
    private MenuProduct dummyMenuProduct;
    private Product dummyProduct;
    private final static long ANY_MENU_ID = 1L;
    private final static long ANY_MENU_GROUP_ID = 1L;
    private final static long ANY_PRODUCT_ID = 1L;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = MenuGroup.of("menuGroupName");
        ReflectionTestUtils.setField(menuGroup, "id", ANY_MENU_GROUP_ID);

        menu = Menu.of("tomato pasta", BigDecimal.ZERO, menuGroup, new ArrayList<>());
        ReflectionTestUtils.setField(menu, "id", ANY_MENU_ID);

        dummyProduct = new Product();
        dummyProduct.setId(ANY_PRODUCT_ID);
        dummyProduct.setName("rice");
        dummyProduct.setPrice(BigDecimal.valueOf(10L));

        dummyMenuProduct = MenuProduct.of(menu, dummyProduct, 1L);
    }

    @Test
    @DisplayName("메뉴는 이름, 가격, 메뉴 그룹 그리고 0개 이상의 메뉴 상품으로 구성된다.")
    void menu_create() {
        assertThat(menu.getName()).isNotNull();
        assertThat(menu.getPrice()).isNotNull();
        assertThat(menu.getMenuGroup()).isNotNull();
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        assertThat(menuProducts).isNotNull();
    }

    @Test
    @DisplayName("메뉴은 등록할 수 잇다.")
    void create_test() {
        given(menuGroupDao.existsById(ANY_MENU_GROUP_ID))
                .willReturn(true);
        given(menuDao.save(menu)).willReturn(menu);

        Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu).isEqualTo(menu);
    }

    @Test
    @DisplayName("메뉴의 가격이 0원 이하일 경우 메뉴를 등록할 수 없다")
    void exception_when_price_is_under_zero() {
        menu.changePrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("price");
    }

    @Test
    @DisplayName("메뉴를 등록하는 시점에 메뉴 그룹이 미리 등록되어 있어야 한다.")
    void menuGroup() {
        given(menuGroupDao.existsById(ANY_MENU_GROUP_ID)).willReturn(false);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("menuGroup");
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴그룹의 가격보다 높을 경우 등록될 수 없다.")
    void price() {
        given(menuGroupDao.existsById(ANY_MENU_GROUP_ID)).willReturn(true);
        given(productDao.findById(ANY_PRODUCT_ID)).willReturn(Optional.of(dummyProduct));

        dummyProduct.setPrice(BigDecimal.valueOf(10L));
        menu.changePrice(BigDecimal.valueOf(100L));
        menu.addMenuProducts(dummyMenuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Total Price");
    }
}