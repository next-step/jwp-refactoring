package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.menu1;
import static kitchenpos.fixture.MenuFixture.menu2;
import static kitchenpos.fixture.MenuGroupFixture.한마리메뉴_그룹;
import static kitchenpos.fixture.MenuProductsFixture.menuProducts1;
import static kitchenpos.fixture.MenuProductsFixture.menuProducts2;
import static kitchenpos.fixture.ProductFixture.후라이드치킨;
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

    @BeforeEach
    void setUp() {
        menuDao = mock(MenuDao.class);
        menuGroupDao = mock(MenuGroupDao.class);
        menuProductDao = mock(MenuProductDao.class);
        productDao = mock(ProductDao.class);
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        menu1.setMenuProducts(menuProducts1);
        menu2.setMenuProducts(menuProducts2);
    }

    @DisplayName("메뉴 생성하기")
    @Test
    void createTest() {
        when(menuGroupDao.existsById(2L)).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(후라이드치킨));
        when(menuDao.save(menu1)).thenReturn(menu1);
        assertThat(menuService.create(menu1)).isEqualTo(menu1);
    }

    @DisplayName("메뉴 가격이 0원 미만시 예외 발생")
    @Test
    void exceptionTest1() {
        BigDecimal wrongPrice = BigDecimal.valueOf(-16000L);
        Menu wrongMenu = Menu.of(3L, 후라이드치킨.getName(), wrongPrice, 한마리메뉴_그룹.getId(), menuProducts1);
        assertThatThrownBy(() -> menuService.create(wrongMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 메뉴그룹 없을 때, 예외 발생")
    @Test
    void exceptionTest2() {
        Long wrongId = 100L;
        Menu wrongMenu = Menu.of(3L, 후라이드치킨.getName(), 후라이드치킨.getPrice(), wrongId, menuProducts1);
        assertThatThrownBy(() -> menuService.create(wrongMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장되지 않은 메뉴상품을 가진 메뉴 생성시 예외 발생")
    @Test
    void exceptionTest3() {
        Long wrongId = 100L;
        Menu wrongMenu = Menu.of(3L, 후라이드치킨.getName(), 후라이드치킨.getPrice(), 한마리메뉴_그룹.getId(), null);
        List<MenuProduct> wrongMenuProducts = Lists.newArrayList(MenuProduct.of(1L, wrongMenu.getId(), wrongId, 1L));
        wrongMenu.setMenuProducts(wrongMenuProducts);

        assertThatThrownBy(
            () -> menuService.create(wrongMenu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴상품 목록의 금액의 총합보다 작은 메뉴를 생성시 예외 발생")
    @Test
    void exceptionTest4() {
        Menu wrongMenu = Menu.of(3L, 후라이드치킨.getName(), 후라이드치킨.getPrice().add(BigDecimal.ONE), 한마리메뉴_그룹.getId(), menuProducts1);
        assertThatThrownBy(() -> menuService.create(wrongMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("목록 조회시, 저장된 메뉴그룹 목록 얻기")
    @Test
    void listTest() {
        when(menuDao.findAll()).thenReturn(Lists.newArrayList(menu1, menu2));
        assertThat(menuService.list()).isEqualTo(Lists.newArrayList(menu1, menu2));
    }

}
