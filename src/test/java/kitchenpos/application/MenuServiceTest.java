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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.MenuGroupServiceTest.generateMenuGroup;
import static kitchenpos.application.ProductServiceTest.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴")
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    private MenuGroup menuGroup;

    private Product product1;
    private Product product2;

    private List<MenuProduct> menuProducts1;

    private Menu menu1;
    private Menu menu2;
    private Menu menu3;

    @BeforeEach
    void setUp() {
        menuGroup = generateMenuGroup(1L, "menuGroup");

        product1 = generateProduct(1L, "product1", new BigDecimal(1000));
        product2 = generateProduct(2L, "product2", new BigDecimal(1500));

        menuProducts1 = new ArrayList<>();
        menuProducts1.add(generateMenuProduct(product1.getId(), 1));
        menuProducts1.add(generateMenuProduct(product2.getId(), 1));

        menu1 = generateMenu(1L, "menu1", new BigDecimal(2500), menuGroup.getId(), menuProducts1);
        menu2 = generateMenu(2L, "menu2", new BigDecimal(1000), menuGroup.getId(), menuProducts1);
        menu3 = generateMenu(3L, "menu3", new BigDecimal(1500), menuGroup.getId(), menuProducts1);
    }

    @Test
    @DisplayName("전체 메뉴를 조회할 수 있다.")
    void menuTest1() {
        given(menuDao.findAll()).willReturn(Arrays.asList(menu1, menu2, menu3));
        given(menuProductDao.findAllByMenuId(any(Long.class))).willReturn(menuProducts1);

        List<Menu> products = menuService.list();
        assertThat(products.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("새로운 메뉴를 추가할 수 있다.")
    void menuTest2() {
        given(menuGroupDao.existsById(menu1.getMenuGroupId())).willReturn(true);
        given(productDao.findById(product1.getId())).willReturn(Optional.of(product1));
        given(productDao.findById(product2.getId())).willReturn(Optional.of(product2));
        given(menuDao.save(any(Menu.class))).willReturn(menu1);

        Menu menu = menuService.create(menu1);
        assertThat(menu.getName()).isEqualTo(menu1.getName());
    }

    @Test
    @DisplayName("메뉴 가격은 필수값이며, 음수여서는 안된다.")
    void menuTest3() {
        Menu nullPriceMenu = generateMenu(4L, "nullPriceMenu", null, menuGroup.getId(), menuProducts1);
        Menu negativePriceMenu = generateMenu(5L, "negativePriceMenu", new BigDecimal(-1), menuGroup.getId(), menuProducts1);

        assertThatThrownBy(() -> menuService.create(nullPriceMenu)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> menuService.create(negativePriceMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹으로 요청할 수 없다.")
    void menuTest4() {
        Menu nullMenuGroupMenu = generateMenu(6L, "nullMenuGroupMenu", new BigDecimal(1500), 999L, menuProducts1);

        assertThatThrownBy(() -> menuService.create(nullMenuGroupMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 요청할 수 없다.")
    void menuTest5() {
        List<MenuProduct> notFoundMenuProducts = new ArrayList<>();
        notFoundMenuProducts.add(generateMenuProduct(999L, 1));

        Menu nullMenuGroupMenu = generateMenu(6L, "nullMenuGroupMenu", new BigDecimal(1500), menuGroup.getId(), notFoundMenuProducts);

        assertThatThrownBy(() -> menuService.create(nullMenuGroupMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격은 메뉴 상품들의 가격의 합보다 크면 안된다.")
    void menuTest6() {
        Menu gatherThenPriceGroupMenu = generateMenu(6L, "nullMenuGroupMenu", new BigDecimal(3000), menuGroup.getId(), menuProducts1);

        assertThatThrownBy(() -> menuService.create(gatherThenPriceGroupMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    public static Menu generateMenu(Long id, String name, BigDecimal price,
                                    Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroupId, menuProducts);
    }

    public static MenuProduct generateMenuProduct(Long productId, long quantity) {
        return MenuProduct.of(null, null, productId, quantity);
    }

}