package kitchenpos.ordering.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 관련 테스트")
public class OrderingTest {

    @DisplayName("주문상태를 변경할 수 있다.")
    @Test
    void changeOrderStatusTest() {
        Ordering ordering = new Ordering(null, OrderStatus.COOKING, null, Arrays.asList());

        Assertions.assertThat(ordering.getOrderStatus()).isEqualTo(OrderStatus.COOKING);

        ordering.changeOrderStatusTo(OrderStatus.COMPLETION);

        Assertions.assertThat(ordering.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("이미 완료된 주문은 상태를 변경할 수 없다.")
    @Test
    void 주문상태_변경_실패() {
        Ordering ordering = new Ordering(null, OrderStatus.COMPLETION, null, Arrays.asList());

        Assertions.assertThat(ordering.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> {
            ordering.changeOrderStatusTo(OrderStatus.COMPLETION);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 만든다.")
    @Test
    void create() {
        Ordering order = new Ordering(1L, 1L, Arrays.asList());

        Assertions.assertThat(order.getId()).isEqualTo(1L);
        Assertions.assertThat(order.getOrderTableId()).isEqualTo(1L);
    }

    @DisplayName("주문아이템목록이 없으면 주문을 만들 수 없다.")
    @Test
    void 주문_만들기_실패_1() {
        assertThatThrownBy(() -> {
            new Ordering(1L, 1L, null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
