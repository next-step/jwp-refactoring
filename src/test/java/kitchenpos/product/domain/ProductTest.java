package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 관련 Domain 단위 테스트")
class ProductTest {

    @DisplayName("상품 가격이 null 이거나 0원 미만일 수 없다.")
    @Test
    void validate() {
        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("제육덮밥", -1));
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("제육덮밥", null));
    }
}
