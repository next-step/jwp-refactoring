package kitchenpos.domain;

import static kitchenpos.domain.OrderLineItemTest.*;
import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 컬렉션 단위 테스트")
class OrdersTest {

    @Test
    @DisplayName("진행중인 주문이 있는지 확인")
    void hasOrderInProgress() {
        Order 주문1_COMPLETION = new Order(COMPLETION);
        Order 주문2_COMPLETION = new Order(COMPLETION);
        Order 주문3_COOKING = new Order(COOKING);
        Orders orders = new Orders(Arrays.asList(주문1_COMPLETION, 주문2_COMPLETION, 주문3_COOKING));

        assertThat(orders.hasOrderInProgress()).isTrue();

    }
}
