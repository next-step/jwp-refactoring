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
}
