package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupService menuGroupService;

    @InjectMocks
    private MenuService menuService;

    private final static long ANY_MENU_ID = 1L;
    private final static long ANY_MENU_GROUP_ID = 1L;
    private final static long ANY_PRODUCT_ID = 1L;

    private Menu menu;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.of("menuGroupName");
        ReflectionTestUtils.setField(menuGroup, "id", ANY_MENU_GROUP_ID);

        menu = Menu.of("tomato pasta", BigDecimal.ZERO, menuGroup, new ArrayList<>());
        ReflectionTestUtils.setField(menu, "id", ANY_MENU_ID);

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
        given(menuGroupService.isExists(menuGroup))
                .willReturn(false);
        given(menuDao.save(menu)).willReturn(menu);

        Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu).isEqualTo(menu);
    }

    @Test
    @DisplayName("메뉴를 등록하는 시점에 메뉴 그룹이 미리 등록되어 있어야 한다.")
    void menuGroup() {
        given(menuGroupService.isExists(menuGroup)).willReturn(true);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("menuGroup");
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴그룹의 가격보다 높을 경우 등록될 수 없다.")
    void price() {
        given(menuGroupService.isExists(menuGroup)).willReturn(false);

        menu.changePrice(BigDecimal.valueOf(100L));

        Product dummyProduct = Product.of("rice", BigDecimal.valueOf(10L));
        ReflectionTestUtils.setField(dummyProduct, "id", ANY_PRODUCT_ID);

        MenuProduct dummyMenuProduct = MenuProduct.of(menu, dummyProduct, 1L);
        menu.addMenuProducts(dummyMenuProduct);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Total Price");
    }
}