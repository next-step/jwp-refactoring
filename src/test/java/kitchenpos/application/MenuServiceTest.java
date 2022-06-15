package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private MenuGroup 초밥_메뉴그룹;
    private Product 우아한_초밥_1;
    private Product 우아한_초밥_2;
    private MenuProduct A_우아한_초밥_1;
    private MenuProduct A_우아한_초밥_2;
    private Menu A;

    @BeforeEach
    void setUp() {
        초밥_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "초밥_메뉴그룹");
        우아한_초밥_1 = ProductFixtureFactory.create(1L, "우아한_초밥_1", BigDecimal.valueOf(10_000));
        우아한_초밥_2 = ProductFixtureFactory.create(2L, "우아한_초밥_2", BigDecimal.valueOf(20_000));
        A = MenuFixtureFactory.create("A", BigDecimal.valueOf(30_000), 초밥_메뉴그룹.getId());

        A_우아한_초밥_1 = MenuProductFixtureFactory.create(1L, A.getId(), 우아한_초밥_1.getId(), 1);
        A_우아한_초밥_2 = MenuProductFixtureFactory.create(2L, A.getId(), 우아한_초밥_2.getId(), 2);

        A.setMenuProducts(Lists.newArrayList(A_우아한_초밥_1, A_우아한_초밥_2));
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create01() {
        // given
        Menu menu = new Menu();
        menu.setName("A");
        menu.setPrice(BigDecimal.valueOf(30_000));
        menu.setMenuGroupId(초밥_메뉴그룹.getId());
        menu.setMenuProducts(Lists.newArrayList(A_우아한_초밥_1, A_우아한_초밥_2));

        given(menuGroupDao.existsById(초밥_메뉴그룹.getId())).willReturn(true);
        given(productDao.findById(A_우아한_초밥_1.getProductId())).willReturn(Optional.ofNullable(우아한_초밥_1));
        given(productDao.findById(A_우아한_초밥_2.getProductId())).willReturn(Optional.ofNullable(우아한_초밥_2));
        given(menuDao.save(any(Menu.class))).willReturn(A);

        // when
        Menu savedMenu = menuService.create(A);

        // then
        assertThat(savedMenu).isEqualTo(A);
    }

    @DisplayName("메뉴 가격은 0원 이상이어야 한다. (null)")
    @ParameterizedTest
    @NullSource
    void create02(BigDecimal price) {
        // given
        Menu menu = new Menu();
        menu.setName("A");
        menu.setPrice(price);
        menu.setMenuGroupId(초밥_메뉴그룹.getId());
        menu.setMenuProducts(Lists.newArrayList(A_우아한_초밥_1, A_우아한_초밥_2));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 가격은 0원 이상이어야 한다. (0원 이하)")
    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    void create03(long price) {
        // given
        Menu menu = new Menu();
        menu.setName("A");
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(초밥_메뉴그룹.getId());
        menu.setMenuProducts(Lists.newArrayList(A_우아한_초밥_1, A_우아한_초밥_2));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴는 반드시 메뉴 그룹에 포함되어야 한다.")
    @Test
    void create04() {
        // given
        Menu menu = new Menu();
        menu.setName("A");
        menu.setPrice(BigDecimal.valueOf(30_000));
        menu.setMenuGroupId(초밥_메뉴그룹.getId());
        menu.setMenuProducts(Lists.newArrayList(A_우아한_초밥_1, A_우아한_초밥_2));

        given(menuGroupDao.existsById(초밥_메뉴그룹.getId())).willReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴를 구성하는 상품이 없는 경우 메뉴로 등록할 수 없다.")
    @Test
    void create05() {
        // given
        Menu menu = new Menu();
        menu.setName("A");
        menu.setPrice(BigDecimal.valueOf(30_000));
        menu.setMenuGroupId(초밥_메뉴그룹.getId());
        menu.setMenuProducts(Lists.newArrayList(A_우아한_초밥_1, A_우아한_초밥_2));

        given(menuGroupDao.existsById(초밥_메뉴그룹.getId())).willReturn(true);
        given(productDao.findById(A_우아한_초밥_1.getProductId())).willThrow(IllegalArgumentException.class);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 가격은 메뉴를 구성하는 상품 가격 * 수량의 합보다 클 수 없다")
    @Test
    void create06() {
        // given
        Menu menu = new Menu();
        menu.setName("A");
        menu.setPrice(BigDecimal.valueOf(10_000_000));
        menu.setMenuGroupId(초밥_메뉴그룹.getId());
        menu.setMenuProducts(Lists.newArrayList(A_우아한_초밥_1, A_우아한_초밥_2));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        given(menuDao.findAll()).willReturn(Lists.newArrayList(A));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).containsExactly(A);
    }
}
