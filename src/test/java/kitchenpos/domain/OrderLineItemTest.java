package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.domain.OrderEntity;
import kitchenpos.order.domain.OrderLineItemEntity;
import kitchenpos.table.domain.OrderTableEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문항목에 대한 단위 테스트")
class OrderLineItemTest {

    private OrderTableEntity 주문_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블 = OrderTableEntity.of(null, 1, false);
    }

    @DisplayName("주문 항목에 주문을 매핑하면 양방향으로 매핑된다")
    @Test
    void map_into_test() {
        // given
        OrderLineItemEntity 주문_항목 = OrderLineItemEntity.of(1L, null, 1L, 1);
        OrderEntity 주문 = OrderEntity.of(null, 주문_테이블);

        // when
        주문_항목.mapIntoOrder(주문);

        // then
        assertThat(주문_항목.getOrder()).isEqualTo(주문);
        assertThat(주문.getOrderLineItems()).contains(주문_항목);
    }

    @DisplayName("주문항목내의 유효성검사시 메뉴가 없으면 예외가 발생한다")
    @Test
    void z() {
        // given
        OrderLineItemEntity 주문_항목 = OrderLineItemEntity.of(1L, null, null, 1);

        // then
        assertThatThrownBy(주문_항목::validateMenu)
            .isInstanceOf(IllegalArgumentException.class);
    }
}
