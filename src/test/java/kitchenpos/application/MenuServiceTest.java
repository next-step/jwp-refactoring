package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.MenuFixture.메뉴_데이터_생성;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_데이터_생성;
import static kitchenpos.fixture.ProductFixture.상품_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    private Menu A_세트;
    private MenuProduct A_세트_감자_튀김;
    private MenuProduct A_세트_햄버거;
    private MenuProduct A_세트_치즈볼;
    private Product 감자_튀김;
    private Product 햄버거;
    private Product 치즈볼;

    @BeforeEach
    void setUp() {
        감자_튀김 = 상품_데이터_생성(1L, "감자튀김", BigDecimal.valueOf(1500));
        햄버거 = 상품_데이터_생성(2L, "햄버거", BigDecimal.valueOf(3500));
        치즈볼 = 상품_데이터_생성(3L, "피자", BigDecimal.valueOf(1000));
        A_세트_감자_튀김 = 메뉴_상품_데이터_생성(1L, 1L, 1L, 1);
        A_세트_햄버거 = 메뉴_상품_데이터_생성(2L, 1L, 2L, 1);
        A_세트_치즈볼 = 메뉴_상품_데이터_생성(3L, 1L, 3L, 1);
    }


    @DisplayName("메뉴 생성")
    @Test
    void create() {
        // given
        A_세트 = 메뉴_데이터_생성(1L, "A세트", BigDecimal.valueOf(5000), 1L, Arrays.asList(A_세트_감자_튀김, A_세트_햄버거));
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(감자_튀김));
        given(productDao.findById(any())).willReturn(Optional.of(햄버거));
        given(menuDao.save(any())).willReturn(A_세트);
        given(menuProductDao.save(A_세트_감자_튀김)).willReturn(A_세트_감자_튀김);
        given(menuProductDao.save(A_세트_햄버거)).willReturn(A_세트_햄버거);

        // when
        Menu menu = menuService.create(A_세트);

        // then
        assertAll(
                () -> assertThat(menu).isEqualTo(A_세트),
                () -> assertThat(menu.getMenuProducts()).containsExactly(A_세트_감자_튀김, A_세트_햄버거)
        );
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void list() {
        // given
        A_세트 = 메뉴_데이터_생성(1L, "A세트", BigDecimal.valueOf(5000), 1L, Arrays.asList(A_세트_감자_튀김, A_세트_햄버거));
        given(menuDao.findAll()).willReturn(Collections.singletonList(A_세트));
        given(menuProductDao.findAllByMenuId(any())).willReturn(Arrays.asList(A_세트_감자_튀김, A_세트_햄버거));

        // when
        List<Menu> menus = menuService.list();

        // then
        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menus).containsExactly(A_세트),
                () -> assertThat(menus.get(0).getMenuProducts()).containsExactly(A_세트_감자_튀김, A_세트_햄버거)
        );
    }

    @Test
    void 메뉴_가격은_0미만일_경우() {
        // given
        A_세트 = 메뉴_데이터_생성(1L, "A세트", BigDecimal.valueOf(-1), 1L, Arrays.asList(A_세트_감자_튀김, A_세트_햄버거));

        // when & then
        assertThatThrownBy(() -> menuService.create(A_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록할_메뉴_그룹이_없는_경우() {
        // given
        A_세트 = 메뉴_데이터_생성(1L, "A세트", BigDecimal.valueOf(5000), 1L, Arrays.asList(A_세트_감자_튀김, A_세트_햄버거));
        given(menuGroupDao.existsById(A_세트.getMenuGroupId())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> menuService.create(A_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록된_상품으로만_메뉴을_구성해야한다() {
        // given
        A_세트 = 메뉴_데이터_생성(1L, "A세트", BigDecimal.valueOf(5000), 1L, Arrays.asList(A_세트_감자_튀김, A_세트_햄버거));
        given(menuGroupDao.existsById(A_세트.getMenuGroupId())).willReturn(true);
        given(productDao.findById(감자_튀김.getId())).willReturn(Optional.of(감자_튀김));
        given(productDao.findById(햄버거.getId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> menuService.create(A_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_구성_상품들의_합보다_큰_경우() {
        // given
        A_세트 = 메뉴_데이터_생성(1L, "A세트", BigDecimal.valueOf(5000), 1L, Arrays.asList(A_세트_감자_튀김, A_세트_치즈볼));
        given(menuGroupDao.existsById(A_세트.getMenuGroupId())).willReturn(true);
        given(productDao.findById(감자_튀김.getId())).willReturn(Optional.of(감자_튀김));
        given(productDao.findById(치즈볼.getId())).willReturn(Optional.of(치즈볼));

        // when & then
        assertThatThrownBy(() -> menuService.create(A_세트))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
