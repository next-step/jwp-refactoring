package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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

    private Long 한마리메뉴_그룹_아이디 = 1L;
    private Long 후라이드_상품_아이디 = 1L;
    private Menu 후라이드치킨_등록_요청;

    @BeforeEach
    void setUp() {
        후라이드치킨_등록_요청 = new Menu("후라이드치킨",
                BigDecimal.valueOf(16_000),
                한마리메뉴_그룹_아이디,
                Arrays.asList(new MenuProduct(null, 후라이드_상품_아이디, 2)));
    }

    @Test
    void 메뉴를_등록시_등록에_성공하고_메뉴_정보를_반환한다() {
        // given
        given(menuGroupDao.existsById(한마리메뉴_그룹_아이디)).willReturn(true);
        given(productDao.findById(후라이드_상품_아이디)).willReturn(Optional.of(ProductFixture.후라이드));
        given(menuDao.save(any())).willReturn(후라이드치킨_등록_요청);
        given(menuProductDao.save(any())).willReturn(new MenuProduct(1L, 후라이드_상품_아이디, 2));

        // when
        Menu 후라이드치킨 = menuService.create(후라이드치킨_등록_요청);

        // then
        메뉴_등록됨(후라이드치킨);
    }

    @Test
    void 메뉴_등록시_가격이_누락되어_있으면_예외처리되어_등록에_실패한다() {
        // given
        후라이드치킨_등록_요청.setPrice(null);

        // when & then
        assertThatThrownBy(() -> menuService.create(후라이드치킨_등록_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_등록시_가격이_0원_미만인경우_예외처리되어_등록에_실패한다() {
        // given
        후라이드치킨_등록_요청.setPrice(BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> menuService.create(후라이드치킨_등록_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_등록시_메뉴_그룹에_속해있지않은경우_예외처리되어_등록에_실패한다() {
        // given
        후라이드치킨_등록_요청.setMenuGroupId(null);

        // when & then
        assertThatThrownBy(() -> menuService.create(후라이드치킨_등록_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_등록시_메뉴에_있는_상품이_미등록인경우_예외처리되어_등록에_실패한다() {
        // given
        given(menuGroupDao.existsById(한마리메뉴_그룹_아이디)).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> menuService.create(후라이드치킨_등록_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(productDao).should(times(1)).findById(any());
    }

    @Test
    void 메뉴_등록시_메뉴가격이_등록된_상품의_요금이_합산된_금액보다_클경우_예외처리되어_등록에_실패한다() {
        // given
        given(menuGroupDao.existsById(한마리메뉴_그룹_아이디)).willReturn(true);
        given(productDao.findById(후라이드_상품_아이디)).willReturn(Optional.of(ProductFixture.후라이드));
        후라이드치킨_등록_요청.setPrice(BigDecimal.valueOf(35_000));

        // when
        assertThatThrownBy(() -> menuService.create(후라이드치킨_등록_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(menuGroupDao).should(times(1)).existsById(한마리메뉴_그룹_아이디);
        then(productDao).should(times(1)).findById(후라이드_상품_아이디);
    }

    @Test
    void 메뉴_목록_조회시_등록된_메뉴_목록을_반환한다() {
        // given
        Menu 후라이드치킨 = 후라이드치킨_등록_요청;
        given(menuDao.findAll()).willReturn(Arrays.asList(후라이드치킨));

        // when
        List<Menu> menus = menuService.list();

        // then
        메뉴_목록_조회됨(menus, 후라이드치킨.getName());
    }


    private void 메뉴_등록됨(Menu menu) {
        then(menuGroupDao).should(times(1)).existsById(한마리메뉴_그룹_아이디);
        then(productDao).should(times(1)).findById(후라이드_상품_아이디);
        then(menuDao).should(times(1)).save(any());
        then(menuProductDao).should(times(1)).save(any());
        assertThat(menu).isNotNull();
    }

    private void 메뉴_목록_조회됨(List<Menu> menus, String... expectedMenuNames) {
        List<String> menuNames = menus.stream()
                .map(Menu::getName)
                .collect(Collectors.toList());
        assertThat(menuNames).containsExactly(expectedMenuNames);
    }
}
