package kitchenpos.domain.product;

import static kitchenpos.application.fixture.ProductFixture.마이너스_가격_상품;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.InvalidParameterException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("상품의 가격은 0원 미만(음수)이면 상품을 등록 할 수 없다.")
    void 상품_가격이_음수인경우_실패한다() {
        // when
        ThrowableAssert.ThrowingCallable actual = () -> 마이너스_가격_상품();

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }
}
