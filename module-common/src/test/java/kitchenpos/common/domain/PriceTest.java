package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("가격 관련 Domain 단위 테스트")
class PriceTest {

    @DisplayName("가격은 0원 미만 이거나 null 일 수 없다.")
    @Test
    void validate() {

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Price(-1000));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Price(null));
    }

}
