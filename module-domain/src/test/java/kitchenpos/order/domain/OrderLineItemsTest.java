package kitchenpos.order.domain;

import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.common.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("주문 라인 아이템 도메인 테스트")
public class OrderLineItemsTest {
    @DisplayName("주문 라인 1개 미만 예외")
    @Test
    void 주문_라인_1개미만_검증() {
        Throwable thrown = catchThrowable(() -> OrderLineItems.of(new ArrayList<>()));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 라인은 최소 1개 이상 필요합니다.");
    }
}
