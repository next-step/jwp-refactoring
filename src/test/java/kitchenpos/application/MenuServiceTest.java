package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private Menu 메뉴1;
    private MenuGroup 메뉴그룹1;
    private MenuProduct 메뉴상품1;
    private MenuProduct 메뉴상품2;
    private Product 상품1;
    private Product 상품2;

    @BeforeEach
    void before() {
        메뉴그룹1 = MenuGroupFixtureFactory.create(1L, "메뉴그룹1");
        메뉴1 = MenuFixtureFactory.create(1L, "메뉴1", BigDecimal.valueOf(3000), 메뉴그룹1.getId());

        상품1 = ProductFixtureFactory.create(1L, "상품1", BigDecimal.valueOf(1000));
        상품2 = ProductFixtureFactory.create(2L, "상품2", BigDecimal.valueOf(2000));

        메뉴상품1 = MenuProductFixtureFactory.create(1L, 메뉴1.getId(), 상품1.getId(), 3);
        메뉴상품2 = MenuProductFixtureFactory.create(2L, 메뉴1.getId(), 상품2.getId(), 1);

        메뉴1.setMenuProducts(Arrays.asList(메뉴상품1, 메뉴상품2));
    }

    @Test
    @DisplayName("생성 하려는 메뉴 가격은 null일 수 없다.")
    void createFailTest01() {
        //given
        Menu 잘못된_메뉴 = new Menu(1L, "잘못된 메뉴", null, 메뉴그룹1.getId());

        //when & then
        assertThatThrownBy(
                () -> menuService.create(잘못된_메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("생성 하려는 메뉴 가격은 음수일 수 없다.")
    void createFailTest02() {
        //given
        Menu 잘못된_메뉴 = new Menu(1L, "잘못된 메뉴", BigDecimal.valueOf(-1), 메뉴그룹1.getId());

        //when & then
        assertThatThrownBy(
                () -> menuService.create(잘못된_메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("생성 하려는 메뉴의 메뉴 그룹이 시스템에 존재 하지 않으면 추가 할 수 없다.")
    void createTestFail03() {
        //given
        given(menuGroupDao.existsById(메뉴그룹1.getId())).willReturn(false);

        //when & then
        assertThatThrownBy(
                () -> menuService.create(메뉴1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("생성 하려는 메뉴의 메뉴 상품이 시스템에 등록 되어 있지 않으면 추가 할 수 없다.")
    void createTestFail04() {
        //given
        given(menuGroupDao.existsById(메뉴그룹1.getId())).willReturn(true);
        given(productDao.findById(메뉴상품1.getProductId())).willThrow(IllegalArgumentException.class);

        //when & then
        assertThatThrownBy(
                () -> menuService.create(메뉴1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("생성 하려는 메뉴 가격이 전체 메뉴상품의 전체 금액(가격 * 수량의 총합)보다 클 수 없다.")
    void createTestFail05() {
        //given
        given(menuGroupDao.existsById(메뉴그룹1.getId())).willReturn(true);
        given(productDao.findById(메뉴상품1.getProductId())).willReturn(Optional.of(상품1));
        given(productDao.findById(메뉴상품2.getProductId())).willReturn(Optional.of(상품2));
        Menu 잘못된_메뉴 = new Menu(1L, "잘못된 메뉴", BigDecimal.valueOf(100_000), 메뉴그룹1.getId());
        잘못된_메뉴.setMenuProducts(Arrays.asList(메뉴상품1, 메뉴상품2));
        //when & then
        assertThatThrownBy(
                () -> menuService.create(잘못된_메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 생성 할 수 있다.")
    void createTest() {
        //given
        given(menuGroupDao.existsById(메뉴그룹1.getId())).willReturn(true);
        given(productDao.findById(메뉴상품1.getProductId())).willReturn(Optional.of(상품1));
        given(productDao.findById(메뉴상품2.getProductId())).willReturn(Optional.of(상품2));
        given(menuDao.save(any(Menu.class))).willReturn(메뉴1);


        //when
        Menu menu = menuService.create(메뉴1);

        //then
        assertThat(menu).isEqualTo(메뉴1);
    }

    @Test
    @DisplayName("메뉴의 목록을 조회 할 수 있다.")
    void listTest() {
        //given
        given(menuDao.findAll()).willReturn(Arrays.asList(메뉴1));

        //when
        List<Menu> menus = menuService.list();

        //then
        assertThat(menus).containsExactly(메뉴1);
    }
}
