package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderTest.주문_항목_목록;
import static kitchenpos.order.domain.OrderTest.주문테이블;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 목록 테스트")
public class OrdersTest {

    @DisplayName("완료되지 않은 주문이 존재한다.")
    @Test
    void notExistCompleteOrder() {
        Order order = new Order(1L, 주문테이블, OrderStatus.COOKING, LocalDateTime.now(), 주문_항목_목록);
        Orders orders = new Orders(new ArrayList<>(Arrays.asList(order)));
        assertThat(orders.notExistCompleteOrder()).isTrue();
    }

}
