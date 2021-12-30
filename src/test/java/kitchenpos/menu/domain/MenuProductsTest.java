package kitchenpos.menu.domain;

import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@DisplayName("메뉴 상품들 테스트")
class MenuProductsTest {

    MenuGroup 두마리치킨;
    Product 후라이드치킨;
    Product 양념치킨;

    @BeforeEach
    void setUp() {
        후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(20000));
        양념치킨 = ProductFixture.of("양념치킨", BigDecimal.valueOf(10000));
        두마리치킨 = MenuGroupFixture.from("두마리치킨");
    }

    @Test
    void 메뉴_상품들_생성() {
        // given - when
        MenuProducts actual = MenuProducts.from(Collections.singletonList(MenuProduct.of(후라이드치킨, 2L)));

        // then
        Assertions.assertThat(actual).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 메뉴_상품들_생성시_상품은_필수이다(List<MenuProduct> input) {
        // given - when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> MenuProducts.from(input);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("메뉴 상품은 필수입니다.");
    }

    @Test
    void 메뉴_상품들의_총합을_구한다() {
        // given
        List<MenuProduct> menuProducts = Arrays.asList(
                MenuProduct.of(후라이드치킨, 2L),
                MenuProduct.of(양념치킨, 2L));

        // when
        BigDecimal actual = MenuProducts.from(menuProducts).calculatorTotalPrice();

        // then
        Assertions.assertThat(actual).isEqualTo(BigDecimal.valueOf(60000));
    }

    @Test
    void 메뉴_변경시_메뉴는_필수이다() {
        // given
        MenuProducts actual = MenuProducts.from(Collections.singletonList(MenuProduct.of(후라이드치킨, 2L)));

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> actual.changeMenu(null);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("메뉴는 필수입니다.");
    }
}
