package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ProductTest {
    @DisplayName("가격이 없는 상품은 생성할 수 없다.")
    @Test
    void 가격이_없는_상품_생성() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("상품", null));
    }

    @DisplayName("가격이 0원 미만인 상품은 생성할 수 없다.")
    @ParameterizedTest(name = "등록하고자 하는 상품의 가격: {0}")
    @ValueSource(longs = {-5, -100})
    void 가격이_음수인_상품_생성(long price) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("감자튀김", BigDecimal.valueOf(price)));
    }
}
