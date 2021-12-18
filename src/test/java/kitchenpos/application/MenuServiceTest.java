package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.*;
import java.util.*;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import kitchenpos.dao.*;
import kitchenpos.domain.*;

@DisplayName("메뉴 관련 테스트")
class MenuServiceTest {
    private MenuDao menuDao;
    private MenuGroupDao menuGroupDao;
    private MenuProductDao menuProductDao;
    private ProductDao productDao;
    private MenuService menuService;

    private Product product1;
    private Product product2;
    private Menu menu1;
    private Menu menu2;
    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts1;
    private List<MenuProduct> menuProducts2;

    @BeforeEach
    void setUp() {
        menuDao = mock(MenuDao.class);
        menuGroupDao = mock(MenuGroupDao.class);
        menuProductDao = mock(MenuProductDao.class);
        productDao = mock(ProductDao.class);
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        menuGroup = MenuGroup.of(2L, "한마리메뉴");

        product1 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(16000L));
        menu1 = Menu.of(1L, product1.getName(), product1.getPrice(), menuGroup.getId(), null);
        menuProducts1 = Lists.newArrayList(MenuProduct.of(1L, menu1.getId(), product1.getId(), 1L));
        menu1.setMenuProducts(menuProducts1);

        product2 = Product.of(2L, "양념치킨", BigDecimal.valueOf(16000L));
        menu2 = Menu.of(1L, product2.getName(), product2.getPrice(), menuGroup.getId(), null);
        menuProducts2 = Lists.newArrayList(MenuProduct.of(2L, menu2.getId(), product2.getId(), 1L));
        menu2.setMenuProducts(menuProducts2);
    }

    @DisplayName("create메서드에 생성을 원하는 Menu 객체를 인자로 하여 호출하면, 생성된 객체를 반환한다.")
    @Test
    void createTest() {
        when(menuGroupDao.existsById(2L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(product1));
        when(menuDao.save(menu1)).thenReturn(menu1);
        assertThat(menuService.create(menu1)).isEqualTo(menu1);
    }

    @DisplayName("메뉴의 가격이 0원 미만인 상품을 포함하는 메뉴를 생성시도 하면 예외를 던진다.")
    @Test
    void exceptionTest1() {
        BigDecimal wrongPrice = BigDecimal.valueOf(-16000L);
        Menu wrongMenu = Menu.of(3L, product1.getName(), wrongPrice, menuGroup.getId(), menuProducts1);
        assertThatThrownBy(() -> menuService.create(wrongMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 메뉴그룹 식별자가 없는 메뉴를 생성시도 하면 예외를 던진다.")
    @Test
    void exceptionTest2() {
        Long wrongId = 100L;
        Menu wrongMenu = Menu.of(3L, product1.getName(), product1.getPrice(), wrongId, menuProducts1);
        assertThatThrownBy(() -> menuService.create(wrongMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴상품 목록의 메뉴상품 식별자가 없는 메뉴를 생성시도 하면 예외를 던진다.")
    @Test
    void exceptionTest3() {
        Long wrongId = 100L;
        Menu wrongMenu = Menu.of(3L, product1.getName(), product1.getPrice(), menuGroup.getId(), null);
        List<MenuProduct> menuProducts = Lists.newArrayList(MenuProduct.of(1L, wrongMenu.getId(), wrongId, 1L));
        wrongMenu.setMenuProducts(menuProducts);

        assertThatThrownBy(
            () -> menuService.create(wrongMenu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴상품 목록의 메뉴상품 금액의 합이 메뉴 금액보다 작은 메뉴를 생성시도 하면 예외를 던진다.")
    @Test
    void exceptionTest4() {
        Menu wrongMenu = Menu.of(3L, product1.getName(), product1.getPrice().add(BigDecimal.ONE), menuGroup.getId(), menuProducts1);
        assertThatThrownBy(() -> menuService.create(wrongMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("list메서드를 호출하면, 기 생성된 MenuGroup 목록을 반환한다.")
    @Test
    void listTest() {
        when(menuDao.findAll()).thenReturn(Lists.newArrayList(menu1, menu2));
        assertThat(menuService.list()).isEqualTo(Lists.newArrayList(menu1, menu2));
    }

}
