package kitchenpos.ordering.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 테스트")
public class OrderingTest {

    @DisplayName("주문상태 변경 테스트")
    @Test
    void changeOrderStatusTest() {
        Ordering ordering = new Ordering(null, OrderStatus.COOKING.name(), null, Arrays.asList());

        assertThat(ordering.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());

        ordering.changeOrderStatusTo(OrderStatus.COMPLETION.name());

        assertThat(ordering.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

}
