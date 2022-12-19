package kitchenpos.product.domain;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 상품_가격은_0원_이상_이어야_한다() {
        BigDecimal 상품가격 = new BigDecimal(-1);

        ThrowingCallable 상품_0원_이하의_가격지정 = () -> new Product(1L, "후라이드치킨", 상품가격);

        Assertions.assertThatIllegalArgumentException().isThrownBy(상품_0원_이하의_가격지정);
    }

    @Test
    void 상품_가격이_null_이면_안된다() {
        BigDecimal 상품가격 = null;

        ThrowingCallable 상품_가격_null_지정 = () -> new Product(1L, "후라이드치킨", 상품가격);

        Assertions.assertThatIllegalArgumentException().isThrownBy(상품_가격_null_지정);
    }

    @Test
    void 상품명은_필수로_입력해야_한다() {
        ThrowingCallable 상품명이_입력되지_않은_경우 = () -> new Product(1L, null, new BigDecimal(16000));

        Assertions.assertThatIllegalArgumentException().isThrownBy(상품명이_입력되지_않은_경우);
    }
}
