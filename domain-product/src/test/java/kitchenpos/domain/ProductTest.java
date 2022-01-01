package kitchenpos.domain;

import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DisplayName("상품 테스트")
class ProductTest {

    @Test
    void 상품_등록() {
        // given - when
        Product actual = Product.of("후라이드치킨", BigDecimal.valueOf(16000));

        // then
        Assertions.assertThat(actual).isNotNull();
    }

    @Test
    void 상품_등록_시_상품의_이름과_가격은_필수이다() {
        // given - when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> Product.of(null, null);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 상품_등록_시_가격은_0원_이상이어야_한다() {
        // given - when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> Product.of("후라이드치킨", BigDecimal.valueOf(-1));

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("상품의 가격은 0원 이상 이어야 합니다.");
    }
}
