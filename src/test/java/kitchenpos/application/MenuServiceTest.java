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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

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

    private Product 허니콤보;
    private Product 뿌링클순살;
    private Product 치즈볼;
    private Product 콜라;
    private MenuGroup 인기그룹;
    private MenuProduct 허니콤보_상품;
    private MenuProduct 허니콤보_치즈볼;
    private MenuProduct 뿌링클순살_상품;
    private MenuProduct 뿌링클순살_콜라;
    private Menu 허니콤보세트;
    private Menu 뿌링클순살세트;

    @BeforeEach
    void setUp() {
        인기그룹 = MenuGroup.of(1L, "인기그룹");
        허니콤보 = Product.of(1L, "허니콤보", BigDecimal.valueOf(20000));
        뿌링클순살 = Product.of(2L, "뿌링클순살", BigDecimal.valueOf(22000));
        치즈볼 = Product.of(3L, "치즈볼", BigDecimal.valueOf(5000));
        콜라 = Product.of(4L, "콜라", BigDecimal.valueOf(2000));
        허니콤보_상품 = MenuProduct.of(1L, 1L, 허니콤보.getId(), 1);
        허니콤보_치즈볼 = MenuProduct.of(2L, 1L, 치즈볼.getId(), 1);
        뿌링클순살_상품 = MenuProduct.of(3L, 2L, 뿌링클순살.getId(), 1);
        뿌링클순살_콜라 = MenuProduct.of(4L, 2L, 콜라.getId(), 1);
        허니콤보세트 = Menu.of(1L, "허니콤보세트", BigDecimal.valueOf(22000), 인기그룹.getId(), Arrays.asList(허니콤보_상품, 허니콤보_치즈볼));
        뿌링클순살세트 = Menu.of(2L, "뿌링클순살세트", BigDecimal.valueOf(24000), 인기그룹.getId(), Arrays.asList(뿌링클순살_상품, 뿌링클순살_콜라));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        // given
        when(menuGroupDao.existsById(인기그룹.getId())).thenReturn(true);
        when(productDao.findById(허니콤보.getId())).thenReturn(Optional.of(허니콤보));
        when(productDao.findById(치즈볼.getId())).thenReturn(Optional.of(치즈볼));
        when(menuDao.save(허니콤보세트)).thenReturn(허니콤보세트);
        when(menuProductDao.save(허니콤보_상품)).thenReturn(허니콤보_상품);
        when(menuProductDao.save(허니콤보_치즈볼)).thenReturn(허니콤보_치즈볼);

        // when
        Menu savedMenu = menuService.create(허니콤보세트);

        // then
        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo(허니콤보세트.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualTo(허니콤보세트.getPrice()),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(허니콤보세트.getMenuGroupId()),
                () -> assertThat(savedMenu.getMenuProducts()).isEqualTo(허니콤보세트.getMenuProducts())
        );
    }

    @DisplayName("메뉴의 가격이 0원 미만인 상품은 생성할 수 없다.")
    @ParameterizedTest(name = "등록하고자 하는 상품의 가격: {0}")
    @ValueSource(longs = {-5, -100})
    void 가격이_음수인_메뉴_생성(long price) {
        // given
        Menu menu = Menu.of(3L, "허니콤보세트", BigDecimal.valueOf(price), 인기그룹.getId(), Arrays.asList(허니콤보_상품, 허니콤보_치즈볼));

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 그룹에 속하지 않은 메뉴는 생성할 수 없다.")
    @Test
    void 메뉴그룹이_없는_메뉴_생성() {
        // given
        Menu menu = Menu.of(4L, "허니콤보세트", BigDecimal.valueOf(20000), null, Arrays.asList(허니콤보_상품, 허니콤보_치즈볼));

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("등록되지 않은 상품으로 메뉴를 생성할 수 없다.")
    @Test
    void 등록되지_않은_상품이_있는_메뉴_생성() {
        // given
        when(menuGroupDao.existsById(인기그룹.getId())).thenReturn(true);
        when(productDao.findById(허니콤보.getId())).thenReturn(Optional.of(허니콤보));

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(허니콤보세트));
    }

    @DisplayName("메뉴 상품들의 가격의 합보다 비싼 메뉴를 생성할 수 없다.")
    @Test
    void 메뉴상품들의_합보다_비싼_메뉴_생성() {
        // given
        when(menuGroupDao.existsById(인기그룹.getId())).thenReturn(true);
        when(productDao.findById(허니콤보.getId())).thenReturn(Optional.of(허니콤보));
        when(productDao.findById(치즈볼.getId())).thenReturn(Optional.of(치즈볼));
        Menu menu = Menu.of(5L, "허니콤보세트", BigDecimal.valueOf(50000), 인기그룹.getId(), Arrays.asList(허니콤보_상품, 허니콤보_치즈볼));

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록_조회() {
        // given
        List<Menu> menus = Arrays.asList(허니콤보세트, 뿌링클순살세트);
        when(menuService.list()).thenReturn(menus);

        // when
        List<Menu> selectMenus = menuService.list();

        // then
        assertAll(
                () -> assertThat(selectMenus).hasSize(menus.size()),
                () -> assertThat(selectMenus).isEqualTo(menus)
        );
    }
}
