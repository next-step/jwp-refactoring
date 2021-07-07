package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 관리")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    private Product 후라이드치킨;
    private Product 양념치킨;
    private Product 족발;
    private MenuProduct 후라이드치킨_세트메뉴;
    private MenuProduct 양념치킨_세트메뉴;
    private MenuProduct 족발_세트메뉴;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
        후라이드치킨 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(10000));
        후라이드치킨_세트메뉴 = MenuProduct.of(1L, null, null, 3);
        양념치킨 = Product.of(2L, "양념치킨", BigDecimal.valueOf(11000));
        양념치킨_세트메뉴 = MenuProduct.of(2L, null, null, 4);
        족발 = Product.of(3L, "족발", BigDecimal.valueOf(15000));
        족발_세트메뉴 = MenuProduct.of(3L, null, null, 5);
    }

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() {
        //given
        Menu menu = Menu.of(
                1L,
                "10만원의 행복 파티 세트 (12인)",
                BigDecimal.valueOf(100000),
                1L,
                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));

        //and
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(후라이드치킨.getId())).willReturn(Optional.of(후라이드치킨));
        given(productDao.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));
        given(productDao.findById(족발.getId())).willReturn(Optional.of(족발));
        given(menuDao.save(menu)).willReturn(menu);
        given(menuProductDao.save(후라이드치킨_세트메뉴)).willReturn(후라이드치킨_세트메뉴);
        given(menuProductDao.save(양념치킨_세트메뉴)).willReturn(후라이드치킨_세트메뉴);
        given(menuProductDao.save(족발_세트메뉴)).willReturn(후라이드치킨_세트메뉴);

        //when
        Menu actual = menuService.create(menu);

        //then
        assertThat(actual).isEqualTo(menu);
    }

    @DisplayName("메뉴의 이름을 지정해야한다.")
    @Test
    void createMenuExceptionIfNameIsNull() {
        //TODO: 추가 기능 개발
    }

    @DisplayName("메뉴의 총 가격은 0원 이상이어야한다.")
    @Test
    void createMenuExceptionIfPriceIsNull() {
        //given
        Menu menu = Menu.of(
                1L,
                "10만원의 행복 파티 세트 (12인)",
                BigDecimal.valueOf(-1000),
                1L,
                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));

        //when
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("메뉴는 속할 메뉴 그룹을 지정해야한다.")
    @Test
    void createMenuExceptionIfMenuGroupIsNull() {
        //given
        Menu menu = Menu.of(
                1L,
                "10만원의 행복 파티 세트 (12인)",
                BigDecimal.valueOf(100000),
                null,
                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));

        //and
        given(menuGroupDao.existsById(any())).willReturn(false);

        //when
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("메뉴는 존재하는 상품으로 구성해야한다.")
    @Test
    void createMenuExceptionIfMenuProductIsNotExist() {
        //given
        Menu menu = Menu.of(
                1L,
                "10만원의 행복 파티 세트 (12인)",
                BigDecimal.valueOf(100000),
                1L,
                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));

        //and
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("메뉴의 메뉴상품은 갯수를 지정해야한다.")
    @Test
    void createMenuExceptionIfMenuProductQuantityIsNull() {
        //TODO: 추가 기능 개발
    }

    @DisplayName("메뉴의 총 가격이 기존 상품들의 총합 가격보다 비쌀 수 없다.")
    @Test
    void createMenuExceptionIfMenuPriceHigherThanProductTotalPrice() {
        //given
        Menu menu = Menu.of(
                1L,
                "10만원의 행복 파티 세트 (12인)",
                BigDecimal.valueOf(200000),
                1L,
                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));

        //and
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(후라이드치킨.getId())).willReturn(Optional.of(후라이드치킨));
        given(productDao.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));
        given(productDao.findById(족발.getId())).willReturn(Optional.of(족발));

        //when
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("메뉴 그룹을 모두 조회한다.")
    @Test
    void list() {
        //given
        Menu menu1 = Menu.of(
                1L,
                "10만원의 행복 파티 세트 (7인)",
                BigDecimal.valueOf(60000),
                1L,
                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴));
        Menu menu2 = Menu.of(
                2L,
                "10만원의 행복 파티 세트2 (12인)",
                BigDecimal.valueOf(100000),
                1L,
                Lists.list(후라이드치킨_세트메뉴, 양념치킨_세트메뉴, 족발_세트메뉴));
        List<Menu> menus = Lists.list(menu1, menu2);

        //and
        given(menuDao.findAll()).willReturn(menus);

        //when
        List<Menu> actual = menuService.list();

        //then
        assertThat(actual).isEqualTo(menus);
    }
}