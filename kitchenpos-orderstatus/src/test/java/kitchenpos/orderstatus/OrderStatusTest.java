package kitchenpos.orderstatus;

import kitchenpos.orderstatus.domain.OrderStatus;
import kitchenpos.orderstatus.domain.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 상태 테스트")
public class OrderStatusTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        OrderStatus 주문_상태 = new OrderStatus(1L, 1L, Status.COOKING);
        assertThat(주문_상태).isEqualTo(new OrderStatus(1L, 1L, Status.COOKING));
    }
}
