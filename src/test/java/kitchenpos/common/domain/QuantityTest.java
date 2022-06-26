package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("수량 관련 Domain 단위 테스트")
class QuantityTest {

    @DisplayName("수량은 1개 이상이어야 한다.")
    @Test
    void validate() {

        //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Quantity(-100));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Quantity(null));
    }
}
