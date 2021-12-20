package kitchenpos.application;


import static kitchenpos.application.fixture.MenuFixture.메뉴생성;
import static kitchenpos.application.fixture.MenuProductFixture.메뉴상품생성;
import static kitchenpos.application.fixture.ProductFixture.상품생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 관리 기능")
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

    private Product 양념치킨;
    private Menu 메뉴_치킨;

    @BeforeEach
    void setUp() {
        // given
        양념치킨 = 상품생성(1L, "양념치킨", 16000);
        메뉴_치킨 = 메뉴생성(1L, "치킨메뉴", 16000, 1L, 메뉴상품생성(1L, 1L, 1L));
    }

    @Test
    @DisplayName("`메뉴`를 등록할 수 있다.")
    void create() {
        // given
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(양념치킨));
        given(menuDao.save(메뉴_치킨)).willReturn(메뉴_치킨);

        // when
        Menu 메뉴등록됨 = menuService.create(메뉴_치킨);

        // then
        메뉴등록_됨(메뉴등록됨);
    }

    @Test
    @DisplayName("`메뉴`의 목록을 조회할 수 있다.")
    void 메뉴_목록_조회() {
        // given
        given(menuDao.findAll()).willReturn(Collections.singletonList(메뉴_치킨));

        // when
        List<Menu> 메뉴목록 = menuService.list();

        // then
        메뉴목록_조회됨(메뉴목록);
    }


    @Test
    @DisplayName("`메뉴`가 속할 `메뉴그룹`이 필수로 있어야 한다.")
    void 메뉴는_메뉴그룹이_없으면_에러() {
        // given
        메뉴_치킨.setPrice(BigDecimal.valueOf(17000));
        given(menuGroupDao.existsById(any())).willReturn(false);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> menuService.create(메뉴_치킨);

        // then
        메뉴생성_실패(actual);
    }

    @Test
    @DisplayName("가격은 무조건 있어야한다.")
    void 메뉴가격이_없는_경우_에러() {
        // given
        메뉴_치킨.setPrice(null);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> menuService.create(메뉴_치킨);

        // then
        메뉴생성_실패(actual);
    }

    @Test
    @DisplayName("메뉴의 가격은 0원 이상 이어야 한다.")
    void 메뉴가격_음수일_경우_에러() {
        // given
        메뉴_치킨.setPrice(BigDecimal.valueOf(-1000));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> menuService.create(메뉴_치킨);

        // then
        메뉴생성_실패(actual);
    }

    @Test
    @DisplayName("`메뉴`의 가격은 상품목록의 가격(상품가격 * 갯수)의 총합보다 클 수 없다.")
    void 메뉴가격은_상품_전체가격보다_크면_에러() {
        // given
        메뉴_치킨.setPrice(BigDecimal.valueOf(17000));
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(양념치킨));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> menuService.create(메뉴_치킨);

        // then
        메뉴생성_실패(actual);
    }

    private void 메뉴등록_됨(Menu 메뉴등록됨) {
        assertThat(메뉴등록됨).isNotNull();
    }

    private void 메뉴생성_실패(ThrowingCallable actual) {
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    private void 메뉴목록_조회됨(List<Menu> 메뉴목록) {
        assertThat(메뉴목록).isNotEmpty();
    }
}
