package kitchenpos.order.domain;

import static kitchenpos.order.__fixture__.OrderLineItemTestFixture.주문_항목_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {
    private List<OrderLineItem> 주문_항목;

    @BeforeEach
    void setUp() {
        주문_항목 = Arrays.asList(주문_항목_생성(1L, "후라이드치킨", BigDecimal.valueOf(16_000), 1L),
                주문_항목_생성(2L, "양념치킨", BigDecimal.valueOf(17_000), 1L));
    }

    @Test
    @DisplayName("주문 항목이 비어있을 경우 Exception")
    void checkOrderLineIsEmpty() {
        final Order order = new Order(1L);

        assertThatThrownBy(() -> order.checkOrderLineIsEmpty())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태 변경")
    void changeOrderStatus() {
        final Order order = new Order(1L, 주문_항목);
        final Order changeOrder = new Order(1L, OrderStatus.MEAL, LocalDateTime.now(), 주문_항목);

        order.changeOrderStatus(changeOrder);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    @DisplayName("주문 상태가 이미 COMPLETION일 경우 Exception")
    void checkOrderStatusComplete() {
        final Order order = new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now(), 주문_항목);

        assertThatThrownBy(() -> order.checkOrderStatusComplete())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
