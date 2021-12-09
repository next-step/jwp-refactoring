package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixture.*;
import static kitchenpos.application.fixture.MenuGroupFixture.*;
import static kitchenpos.application.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    @BeforeEach
    void setUp() {
        when(menuGroupDao.existsById(고기_메뉴그룹.getId())).thenReturn(true);
        when(productDao.findById(불고기_돼지고기.getProductId())).thenReturn(Optional.ofNullable(돼지고기));
        when(productDao.findById(불고기_공기밥.getProductId())).thenReturn(Optional.ofNullable(공기밥));
        when(menuDao.save(any(Menu.class))).thenReturn(불고기);
    }

    @DisplayName("Menu 를 등록한다.")
    @Test
    void create1() {
        // given
        Menu menu = new Menu();
        menu.setName("불고기");
        menu.setPrice(BigDecimal.valueOf(10_000));
        menu.setMenuGroupId(고기_메뉴그룹.getId());
        menu.setMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu).isEqualTo(불고기);
    }

    @DisplayName("Menu 가격은 null 이면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        Menu menu = new Menu();
        menu.setName("불고기");
        menu.setPrice(null);
        menu.setMenuGroupId(고기_메뉴그룹.getId());
        menu.setMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("Menu 가격은 음수(0원 미만)이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10, -100})
    void create3(int wrongPrice) {
        // given
        Menu menu = new Menu();
        menu.setName("불고기");
        menu.setPrice(BigDecimal.valueOf(wrongPrice));
        menu.setMenuGroupId(고기_메뉴그룹.getId());
        menu.setMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("Menu 는 자신이 속할 MenuGroup 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        Menu menu = new Menu();
        menu.setName("불고기");
        menu.setPrice(BigDecimal.valueOf(10_000));
        menu.setMenuGroupId(고기_메뉴그룹.getId());
        menu.setMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        when(menuGroupDao.existsById(고기_메뉴그룹.getId())).thenReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("Menu 는 메뉴를 구성하는 Product 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create5() {
        // given
        Menu menu = new Menu();
        menu.setName("불고기");
        menu.setPrice(BigDecimal.valueOf(10_000));
        menu.setMenuGroupId(고기_메뉴그룹.getId());
        menu.setMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        when(productDao.findById(돼지고기.getId())).thenThrow(IllegalArgumentException.class);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("Menu 의 총 가격이 메뉴를 구성하는 각 상품의 (가격 * 수량) 총합보다 크면 예외가 발생한다.")
    @Test
    void create6() {
        // given
        Menu menu = new Menu();
        menu.setName("불고기");
        menu.setPrice(BigDecimal.valueOf(1_000_000));
        menu.setMenuGroupId(고기_메뉴그룹.getId());
        menu.setMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("Menu 목록을 조회한다.")
    @Test
    void findList() {
        // given
        when(menuDao.findAll()).thenReturn(Arrays.asList(불고기));
        when(menuProductDao.findAllByMenuId(불고기.getId())).thenReturn(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).containsExactly(불고기);
    }
}