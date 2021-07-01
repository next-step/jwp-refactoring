package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class OrderCreateTest {
    @Test
    @DisplayName("등록을 원하는 주문에 주문 항목이 비어있으면 IllegalArgumentException 이 발생한다.")
    void 등록을_원하는_주문에_주문_항목이_비어있으면_IllegalArgumentException_이_발생한다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderCreate(null, null, null));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderCreate(null, null, Arrays.asList()));
    }

}