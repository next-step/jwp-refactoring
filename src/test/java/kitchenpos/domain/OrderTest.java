package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import kitchenpos.order.domain.OrderEntity;
import kitchenpos.order.domain.OrderLineItemEntity;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문에 대한 단위 테스트")
class OrderTest {
    private OrderTableEntity 주문_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블 = OrderTableEntity.of(null, 1, false);
    }

    @DisplayName("주문을 생성하면 정상적으로 조리중 상태, 주문생성시간이 생성된다")
    @Test
    void create_test() {
        // when
        OrderEntity result = OrderEntity.of(null, 주문_테이블);

        // then
        assertNotNull(result.getOrderedTime());
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문에 주문항목을 매핑시키면 정상적으로 매핑된다")
    @Test
    void map_into_test() {
        // given
        OrderLineItemEntity 주문_항목 = OrderLineItemEntity.of(1L, null, 1L, 1);
        OrderLineItemEntity 주문_항목2 = OrderLineItemEntity.of(2L, null, 2L, 1);
        OrderEntity 주문 = OrderEntity.of(null, 주문_테이블);

        // when
        주문.mapIntoLineItems(Arrays.asList(주문_항목, 주문_항목2));

        // then
        assertThat(주문_항목.getOrder()).isEqualTo(주문);
        assertThat(주문_항목2.getOrder()).isEqualTo(주문);
    }

    @DisplayName("주문의 상태를 변경하면 정상적으로 변경된다")
    @Test
    void change_status_test() {
        // given
        OrderEntity 주문 = OrderEntity.of(null, 주문_테이블);

        // when
        주문.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("주문의 상태를 변경하면 정상적으로 변경된다")
    @Test
    void order_status_exception_test() {
        // given
        OrderEntity 주문 = OrderEntity.of(null, 주문_테이블);
        주문.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThatThrownBy(주문::validateMustNotBeCompletionStatus)
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 주문 테이블을 주문에 넘기면 예외가 발생한다")
    @Test
    void order_exception_test() {
        // given
        OrderTableEntity 빈_주문_테이블 = OrderTableEntity.of(null, 1, true);

        // then
        assertThatThrownBy(() -> {
            OrderEntity.of(null, 빈_주문_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
