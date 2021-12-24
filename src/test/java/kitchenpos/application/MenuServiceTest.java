package kitchenpos.application;

import kitchenpos.common.fixtrue.MenuFixture;
import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.MenuProductFixture;
import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    MenuProductDao menuProductDao;

    @Mock
    ProductDao productDao;

    @InjectMocks
    MenuService menuService;

    MenuGroup 두마리치킨;
    Product 후라이드치킨;
    Menu 후라이드_후라이드;
    MenuProduct 후라이드_후라이드_메뉴_상품;

    @BeforeEach
    void setUp() {
        후라이드치킨 = ProductFixture.of(1L, "후라이드치킨", BigDecimal.valueOf(16000));
        두마리치킨 = MenuGroupFixture.of(1L, "두마리치킨");
        후라이드_후라이드_메뉴_상품 = MenuProductFixture.of(1L, 1L, 후라이드치킨.getId(), 2);
        후라이드_후라이드 = MenuFixture.of(
                1L,
                "후라이드+후라이드",
                BigDecimal.valueOf(31000),
                두마리치킨.getId(),
                후라이드_후라이드_메뉴_상품);
    }

    @Test
    void 메뉴_생성() {
        // given
        given(menuGroupDao.existsById(후라이드_후라이드.getMenuGroupId())).willReturn(true);
        given(productDao.findById(후라이드_후라이드.getMenuProducts().get(0).getProductId())).willReturn(Optional.of(후라이드치킨));
        given(menuDao.save(후라이드_후라이드)).willReturn(후라이드_후라이드);
        given(menuProductDao.save(후라이드_후라이드_메뉴_상품)).willReturn(후라이드_후라이드_메뉴_상품);

        // when
        Menu actual = menuService.create(후라이드_후라이드);

        // then
        assertAll(() -> {
            assertThat(actual).isEqualTo(후라이드_후라이드);
            assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(31000));
        });
    }

    @Test
    void 메뉴_생성_시_가격은_0원_이상이어야_한다() {
        // given
        후라이드_후라이드.setPrice(BigDecimal.valueOf(-1));

        // when
        ThrowingCallable throwingCallable = () -> menuService.create(후라이드_후라이드);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 메뉴_생성_시_등록하려는_메뉴는_메뉴_그룹이_존재해야한다() {
        // given
        후라이드_후라이드.setMenuGroupId(null);

        // then
        ThrowingCallable throwingCallable = () -> menuService.create(후라이드_후라이드);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @DisplayName("등록하려는 `메뉴`의 가격은 `메뉴 상품`들의 수량 * `상품`의 가격을 모두 더한 금액보다 작아야한다")
    @Test
    void 등록하려는_메뉴의_가격은_메뉴_상품_들의_가격을_모두_더한_금액보다_작아야한다() {
        // given
        후라이드_후라이드.setPrice(BigDecimal.valueOf(33000L));
        given(menuGroupDao.existsById(후라이드_후라이드.getMenuGroupId())).willReturn(true);
        given(productDao.findById(후라이드_후라이드.getMenuProducts().get(0).getProductId())).willReturn(Optional.of(후라이드치킨));

        // then
        ThrowingCallable throwingCallable = () -> menuService.create(후라이드_후라이드);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 메뉴_조회() {
        // given
        List<Menu> menus = Collections.singletonList(후라이드_후라이드);
        given(menuDao.findAll()).willReturn(menus);
        given(menuProductDao.findAllByMenuId(후라이드_후라이드.getId())).willReturn(Collections.singletonList(후라이드_후라이드_메뉴_상품));

        // when
        List<Menu> actual = menuService.list();

        // then
        Assertions.assertAll(() -> {
            assertThat(actual).hasSize(1);
            assertThat(actual).containsExactlyElementsOf(Collections.singletonList(후라이드_후라이드));
        });
    }
}
