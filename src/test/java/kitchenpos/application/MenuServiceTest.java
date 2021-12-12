package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuProductDao menuProductDao;

    @InjectMocks
    private MenuService menuService;

    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private Product 공기밥;
    private Menu 불고기;
    private MenuProduct 불고기_돼지고기;
    private MenuProduct 불고기_공기밥;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);
        불고기 = MenuFixtureFactory.create(1L, "불고기", 10_000, 고기_메뉴그룹);

        불고기_돼지고기 = MenuProductFixtureFactory.create(1L, 불고기.getId(), 돼지고기.getId(), 1L);
        불고기_공기밥 = MenuProductFixtureFactory.create(2L, 불고기.getId(), 공기밥.getId(), 1L);
        불고기.setMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));
    }

    @DisplayName("Menu 를 등록한다.")
    @Test
    void create1() {
        // given
        Menu menu = Menu.of("불고기", BigDecimal.valueOf(10_000), 고기_메뉴그룹, Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        given(menuGroupDao.existsById(고기_메뉴그룹.getId())).willReturn(true);
        given(productDao.findById(불고기_돼지고기.getProductId())).willReturn(Optional.ofNullable(돼지고기));
        given(productDao.findById(불고기_공기밥.getProductId())).willReturn(Optional.ofNullable(공기밥));
        given(menuDao.save(any(Menu.class))).willReturn(불고기);

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu).isEqualTo(불고기);
    }

    @DisplayName("Menu 가격은 null 이면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        Menu menu = Menu.of("불고기", null, 고기_메뉴그룹, Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("Menu 가격은 음수(0원 미만)이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10, -100})
    void create3(int wrongPrice) {
        // given
        Menu menu = Menu.of("불고기", BigDecimal.valueOf(wrongPrice), 고기_메뉴그룹, Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("Menu 는 자신이 속할 MenuGroup 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        Menu menu = Menu.of("불고기", BigDecimal.valueOf(10_000), 고기_메뉴그룹, Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        given(menuGroupDao.existsById(고기_메뉴그룹.getId())).willReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("Menu 는 메뉴를 구성하는 Product 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create5() {
        // given
        Menu menu = Menu.of("불고기", BigDecimal.valueOf(10_000), 고기_메뉴그룹, Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        given(menuGroupDao.existsById(고기_메뉴그룹.getId())).willReturn(true);
        given(productDao.findById(불고기_돼지고기.getProductId())).willReturn(Optional.ofNullable(돼지고기));
        given(productDao.findById(돼지고기.getId())).willThrow(IllegalArgumentException.class);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("Menu 의 총 가격이 메뉴를 구성하는 각 상품의 (가격 * 수량) 총합보다 크면 예외가 발생한다.")
    @Test
    void create6() {
        // given
        Menu menu = Menu.of("불고기", BigDecimal.valueOf(1_000_000), 고기_메뉴그룹, Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("Menu 목록을 조회한다.")
    @Test
    void findList() {
        // given
        given(menuDao.findAll()).willReturn(Arrays.asList(불고기));
        given(menuProductDao.findAllByMenuId(불고기.getId())).willReturn(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).containsExactly(불고기);
    }
}