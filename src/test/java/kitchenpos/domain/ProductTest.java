package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 상품_가격은_0원_이상_이어야_한다() {
        BigDecimal 상품가격 = new BigDecimal(-1);

        ThrowingCallable 상품_0원_이하의_가격지정 = () -> new Product(1L, "후라이드치킨", 상품가격);

        assertThatIllegalArgumentException().isThrownBy(상품_0원_이하의_가격지정);
    }

    @Test
    void 상품_가격이_null_이면_안된다() {
        BigDecimal 상품가격 = null;

        ThrowingCallable 상품_가격_null_지정 = () -> new Product(1L, "후라이드치킨", 상품가격);

        assertThatIllegalArgumentException().isThrownBy(상품_가격_null_지정);
    }
}
