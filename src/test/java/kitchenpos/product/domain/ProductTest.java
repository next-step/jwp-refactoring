package kitchenpos.product.domain;

import kitchenpos.common.fixtrue.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("상품 테스트")
class ProductTest {

    @Test
    void 상품_등록() {
        // given - when
        Product actual = ProductFixture.of(1L,"후라이드치킨", BigDecimal.valueOf(16000));

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void 상품_등록_시_상품의_이름과_가격은_필수이다() {
        // given - when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> ProductFixture.of(1L, null, null);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 상품_등록_시_가격은_0원_이상이어야_한다() {
        // given - when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> ProductFixture.of(1L, "후라이드치킨", BigDecimal.valueOf(-1));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("상품의 가격은 0원 이상 이어야 합니다.");
    }
}
