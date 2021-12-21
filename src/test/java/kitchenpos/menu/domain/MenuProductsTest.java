package kitchenpos.menu.domain;

import kitchenpos.menu.fixture.MenuProductFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.fixture.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("메뉴 상품 목록 테스트")
class MenuProductsTest {
    private MenuProduct 강정치킨_메뉴상품;
    private MenuProduct 후라이드치킨_메뉴상품;

    @BeforeEach
    void setup() {
        Product 강정치킨 = ProductFixture.create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        Product 후라이드치킨 = ProductFixture.create(2L, "후라이드치킨", BigDecimal.valueOf(16_000));

        강정치킨_메뉴상품 = MenuProductFixture.create(강정치킨.getId(), 1L);
        후라이드치킨_메뉴상품 = MenuProductFixture.create(후라이드치킨.getId(), 1L);
    }

    @DisplayName("메뉴 상품 목록 생성 확인")
    @Test
    void 메뉴_상품_목록_생성_확인() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuProducts.of(Arrays.asList(강정치킨_메뉴상품, 후라이드치킨_메뉴상품));

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

//    @DisplayName("초과 금액 확인")
//    @Nested
//    class TestIsOverPrice {
//        @DisplayName("요청 확인 금액이 전체 금액보다 작거나 같음")
//        @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
//        @ValueSource(longs = {32_000, 33_000})
//        void 요청_확인_금액이_전체_금액보다_작거나_같음(Long requestPrice) {
//            // given
//            BigDecimal price = BigDecimal.valueOf(requestPrice);
//            MenuProducts 메뉴_상품_목록 = MenuProducts.of(Arrays.asList(강정치킨_메뉴상품, 후라이드치킨_메뉴상품));
//
//            // when
//            boolean 확인_결과 = 메뉴_상품_목록.isOverPrice(price);
//
//            // then
//            assertThat(확인_결과).isFalse();
//        }
//
//        @DisplayName("요청 확인 금액이 전체 금액보다 큼")
//        @Test
//        void 요청_확인_금액이_전체_금액보다_큼() {
//            // given
//            BigDecimal price = BigDecimal.valueOf(34_000);
//            MenuProducts 메뉴_상품_목록 = MenuProducts.of(Arrays.asList(강정치킨_메뉴상품, 후라이드치킨_메뉴상품));
//
//            // when
//            boolean 확인_결과 = 메뉴_상품_목록.isOverPrice(price);
//
//            // then
//            assertThat(확인_결과).isTrue();
//        }
//    }
}
