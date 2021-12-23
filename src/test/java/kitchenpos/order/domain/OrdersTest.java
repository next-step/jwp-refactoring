package kitchenpos.order.domain;

import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrdersTest {

    @DisplayName("주문 목록 일급콜렉션 생성")
    @Test
    void create() {
        Orders orders = new Orders();

        assertThat(orders).isNotNull();
    }

    @DisplayName("주문 목록 추가")
    @Test
    void add() {
        Order 샘플1 = OrderFixture.샘플1();
        Orders orders = new Orders();

        orders.add(샘플1);

        assertThat(orders.getOrders().contains(샘플1)).isTrue();
    }

    @DisplayName("조리 또는 식사 상태이면 빈 테이블로 만들 수 없다.")
    @Test
    void validateCompletion() {
        Order 샘플1 = OrderFixture.샘플1();
        샘플1.updateOrderStatus(OrderStatus.MEAL);
        Orders orders = new Orders();
        orders.add(샘플1);

        assertThatThrownBy(
                () -> orders.validateCompletion()
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
