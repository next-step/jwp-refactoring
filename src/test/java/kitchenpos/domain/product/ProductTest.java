package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("상품의 가격은 0원 미만(음수)이면 상품을 등록 할 수 없다.")
    void 상품_가격이_음수인경우_실패한다() {
        // when
        ThrowableAssert.ThrowingCallable actual = () -> Product.of("후리이드치킨", -15000);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }
}
