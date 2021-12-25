package kitchenpos.product.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.fixture.NameFixture;
import kitchenpos.common.domain.fixture.PriceFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 테스트")
class ProductTest {
    @DisplayName("상품 생성 확인")
    @Test
    void 상품_생성_확인() {
        // given
        Name 상품_이름 = NameFixture.of("강정치킨");
        Price 상품_가격 = PriceFixture.of(BigDecimal.valueOf(17_000L));

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Product.of(상품_이름, 상품_가격);

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }

    @DisplayName("상품 이름은 비어있을 수 없음")
    @Test
    void 상품_이름은_비어있을_수_없음() {
        // given
        Price 상품_가격 = PriceFixture.of(BigDecimal.valueOf(17_000L));

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Product.of(null, 상품_가격);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격은 비어있을 수 없음")
    @Test
    void 상품_가격은_비어있을_수_없음() {
        // given
        Name 상품_이름 = NameFixture.of("강정치킨");

        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> Product.of(상품_이름, null);

        // then
        assertThatThrownBy(생성_요청).isInstanceOf(IllegalArgumentException.class);
    }
}
